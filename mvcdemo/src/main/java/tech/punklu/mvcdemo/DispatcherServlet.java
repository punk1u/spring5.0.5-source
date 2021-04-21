package tech.punklu.mvcdemo;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import tech.punklu.mvcdemo.annotation.Controller;
import tech.punklu.mvcdemo.annotation.RequestMapping;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class DispatcherServlet extends HttpServlet {

	private static  String COMPONENT_SCAN_ELEMENT_PACKAGE_NAME = "package";

	private static  String COMPONENT_SCAN_ELEMENT_NAME = "componentScan";

	private static  String projectPath = DispatcherServlet.class.getResource("/").getPath();

	private static Map<String,Method> methodMap = new HashMap<>();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doPost(req, resp);
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		projectPath = projectPath.replaceAll("%20"," ");
		String initParameter = config.getInitParameter("xmlPath");
		File file = new File(projectPath + "//" + initParameter);
		Document document = parse(file);
		Element rootElement = document.getRootElement();
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
