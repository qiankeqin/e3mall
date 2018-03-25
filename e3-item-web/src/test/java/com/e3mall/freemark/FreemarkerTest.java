package com.e3mall.freemark;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreemarkerTest {
	
	@Test
	public void testFreeMarker() throws Exception{
		//首先需要创建一个模板文件放到目录中
		//创建一个Configuration对象
		Configuration configuration = new Configuration(Configuration.getVersion());
		//设置模板文件保存的目录
		configuration.setDirectoryForTemplateLoading(new File("G:/EclipseWorkstation/e3mall-trunk/e3mall/e3-item-web/src/main/webapp/WEB-INF/ftl"));
		//模板文件的编码格式，一般就是utf-8
		configuration.setDefaultEncoding("utf-8");
		//加载一个模板文件，创建一个模板对象
		Template template = configuration.getTemplate("hello.ftl");
		//创建一个数据集，可以是pojo，也可以是map，推荐使用map
		Map data = new HashMap<>();
		data.put("hello", "hello freemarker");
		//创建一个Student对象
		Student student = new Student();
		//创建一个Writer对象，指定输出文件的路径和文件名
		Writer writer = new FileWriter(new File("G:/EclipseWorkstation/temp/hello.txt"));
		//生成静态页面
		template.process(data, writer);
		//关闭流
		writer.close();
	}
	@Test
	public void testFreeMarkerPOJO() throws Exception{
		//首先需要创建一个模板文件放到目录中
		//创建一个Configuration对象
		Configuration configuration = new Configuration(Configuration.getVersion());
		//设置模板文件保存的目录
		configuration.setDirectoryForTemplateLoading(new File("G:/EclipseWorkstation/e3mall-trunk/e3mall/e3-item-web/src/main/webapp/WEB-INF/ftl"));
		//模板文件的编码格式，一般就是utf-8
		configuration.setDefaultEncoding("utf-8");
		//加载一个模板文件，创建一个模板对象
		Template template = configuration.getTemplate("student.ftl");
		//创建一个数据集，可以是pojo，也可以是map，推荐使用map
		Map data = new HashMap<>();
		//创建一个Student对象
		Student student = new Student(1,"小明",18,"zhejiang hangzhou");
		data.put("student", student);
		
		
		//创建一个Writer对象，指定输出文件的路径和文件名
		Writer writer = new FileWriter(new File("G:/EclipseWorkstation/temp/student.html"));
		//生成静态页面
		template.process(data, writer);
		//关闭流
		writer.close();
	}
	@Test
	public void testFreeMarkerList() throws Exception{
		//首先需要创建一个模板文件放到目录中
		//创建一个Configuration对象
		Configuration configuration = new Configuration(Configuration.getVersion());
		//设置模板文件保存的目录
		configuration.setDirectoryForTemplateLoading(new File("G:/EclipseWorkstation/e3mall-trunk/e3mall/e3-item-web/src/main/webapp/WEB-INF/ftl"));
		//模板文件的编码格式，一般就是utf-8
		configuration.setDefaultEncoding("utf-8");
		//加载一个模板文件，创建一个模板对象
		Template template = configuration.getTemplate("stuList.ftl");
		//创建一个数据集，可以是pojo，也可以是map，推荐使用map
		Map data = new HashMap<>();
		data.put("hello","include 模板");
		//创建一个Student对象
		List<Student> list = new ArrayList<Student>();
		list.add(new Student(1,"小明",18,"zhejiang hangzhou"));
		list.add(new Student(2,"小明2",20,"zhejiang hangzho1u"));
		list.add(new Student(3,"小明3",23,"zhejiang hangzhou22"));
		list.add(new Student(4,"小明4",52,"zhejiang hangzho3u"));
		list.add(new Student(5,"小明5",11,"zhejiang hangzho44u"));
		data.put("stuList", list);
		
		Date date = new Date();
		data.put("date", date);
		
		data.put("val", null);
		
		//创建一个Writer对象，指定输出文件的路径和文件名
		Writer writer = new FileWriter(new File("G:/EclipseWorkstation/temp/stuList.html"));
		//生成静态页面
		template.process(data, writer);
		//关闭流
		writer.close();
	}

}
