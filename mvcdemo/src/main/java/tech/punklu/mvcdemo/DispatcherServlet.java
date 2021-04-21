package tech.punklu.mvcdemo;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import tech.punklu.mvcdemo.annotation.Controller;
import tech.punklu.mvcdemo.annotation.RequestMapping;
import tech.punklu.mvcdemo.annotation.ResponseBody;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class DispatcherServlet extends HttpServlet {

	private static  String COMPONENT_SCAN_ELEMENT_PACKAGE_NAME = "package";

	private static  String COMPONENT_SCAN_ELEMENT_NAME = "componentScan";

	private static  String projectPath = DispatcherServlet.class.getResource("/").getPath();

	private static Map<String,Method> methodMap = new HashMap<>();

	private  static String prefix = "";
	private  static String suffix = "";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
 		doPost(req,resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		/**
		 * 获取到请求的URI
		 */
		String contextPath = req.getContextPath();
		String requestURI = req.getRequestURI();
		requestURI = requestURI.replace(contextPath,"");
		Method method = methodMap.get(requestURI);
		if (method != null){
			/**
			 * 获取方法的参数名称
			 *
			 * 这个使用方式需要：
			 * javac -parameters进行编译
			 * idea中找到File->Settings->java Compiler中的Additional command line parameters添加-parameters参数，然后clean install
			 */
			Parameter[] parameters = method.getParameters();
			/**
			 * 实参数组
			 */
			Object[] objects = new Object[parameters.length];
 			for (int i = 0; i < parameters.length; i++) {
				Parameter parameter = parameters[i];
				String name = parameter.getName();
				Class type = parameter.getType();
				if (type.equals(String.class)){
					objects[i] = req.getParameter(name);
				}else if(type.equals(HttpServletRequest.class)){
					objects[i] = req;
				}else if(type.equals(HttpServletResponse.class)){
					objects[i] = resp;
				}else {
					/**
					 * 处理对象参数，并给对象中的字段赋值
					 */
					try {
						Object o = type.newInstance();
						for(Field field : type.getDeclaredFields()){
							field.setAccessible(true);
							String fieldName = field.getName();
							field.set(o,req.getParameter(fieldName));
						}
						objects[i] = o;
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}

			try {
				Object o= null;
				o = method.getDeclaringClass().newInstance();
				Object invoke = method.invoke(o, objects);
				// 判断返回值是否是Void
				if (!method.getReturnType().equals(Void.class)){
					ResponseBody annotation = method.getAnnotation(ResponseBody.class);
					if (annotation!=null){
						//提供接口来做这个事情
						resp.getWriter().write(String.valueOf(invoke));
					}else {
						/**
						 * 转发请求到指定页面
						 */
						// /page/index.html   page/index.html
						req.getRequestDispatcher(prefix+String.valueOf(invoke)+suffix).forward(req,resp);
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			resp.setStatus(404);
		}

	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		projectPath = projectPath.replaceAll("%20"," ");
		String initParameter = config.getInitParameter("xmlPath");
		File file = new File(projectPath + "//" + initParameter);
		Document document = parse(file);
		Element rootElement = document.getRootElement();
		Element view = rootElement.element("view");
		prefix = view.attribute("prefix").getValue();
		suffix = view.attribute("suffix").getValue();
		Element componentScan = rootElement.element(COMPONENT_SCAN_ELEMENT_NAME);
		String packagePath = componentScan.attribute(COMPONENT_SCAN_ELEMENT_PACKAGE_NAME).getValue();
		packagePath = packagePath.replace(".","\\");
		// 扫描项目
		scanProjectByPath(projectPath + "\\" +packagePath);
	}

	public void  scanProjectByPath(String path){
		File file = new File(path);
		// 递归解析路径下的所有文件
		scanFile(file);
	}

	public void scanFile(File file){
		// 递归解析项目
		if (file.isDirectory()){
			for (File listFile : file.listFiles()) {
				scanFile(listFile);
			}
		}else {
			// 如果不是文件夹
			String filePath = file.getPath();
			String suffix = filePath.substring(filePath.lastIndexOf("."));
			if (suffix.equals(".class")){
				String classPath  =  filePath.replace(new File(projectPath).getPath()+"\\","");
				classPath = classPath.replaceAll("\\\\",".");
				String className = classPath.substring(0,classPath.lastIndexOf("."));
				try {
					Class<?> clazz = Class.forName(className);
					if (clazz.isAnnotationPresent(Controller.class)) {
						RequestMapping classRequestMapping = clazz.getAnnotation(RequestMapping.class);
						String classRequestMappingUrl = "";
						if (classRequestMapping!=null){
							classRequestMappingUrl = classRequestMapping.value();
						}
						for (Method method : clazz.getDeclaredMethods()) {
							if (!method.isSynthetic()) {
								RequestMapping annotation = method.getAnnotation(RequestMapping.class);
								if (annotation != null) {
									String methodRequsetMappingUrl  = "";
									methodRequsetMappingUrl  = annotation.value();
									System.out.println("类:"+clazz.getName()+"的"+method.getName()+"方法被映射到了"+classRequestMappingUrl+methodRequsetMappingUrl+"上面");
									methodMap.put(classRequestMappingUrl+methodRequsetMappingUrl,method);
								}

							}
						}
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Document parse(File file){
		SAXReader saxReader = new SAXReader();
		try {
			return saxReader.read(file);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}
}
