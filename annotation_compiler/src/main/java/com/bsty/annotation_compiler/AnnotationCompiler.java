package com.bsty.annotation_compiler;

import com.bsty.annotation.BindPath;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

/**
 * 替我们生成工具类，同时写代码
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes("com.bsty.annotation.BindPath") //也可以用这种方式
public class AnnotationCompiler extends AbstractProcessor {
    //生成文件的对象
    Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
    }

    /**
     * 声明注解处理器支持的java源版本
     *
     * @return
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return processingEnv.getSourceVersion();
    }

    /**
     * 声明注解处理器要处理的注解
     *
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(BindPath.class.getCanonicalName());
        return types;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //获取到当前模块BindPath的阶段
        //TypeElement 类节点
        //ExecutableElement 方法节点
        //VariableElement 成员变量的节点
        Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(BindPath.class);
        Map<String, String> map = new HashMap<>();
        for (Element element : elementsAnnotatedWith) {
            TypeElement typeElement = (TypeElement) element;
            //获取到activity上BindPath注解
            BindPath annotation = typeElement.getAnnotation(BindPath.class);
            String key = annotation.value();
            //获取到的包名+类名
            Name activityName = typeElement.getQualifiedName();
            map.put(key, activityName + ".class");
        }
        if (map.size() > 0) {
            Writer writer = null;
            String activityName = "ActivityUtil" + System.currentTimeMillis();
            try {
                //生成一个java文件
                JavaFileObject sourceFile = filer.createSourceFile("com.bsty.util." + activityName);
                writer = sourceFile.openWriter();
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("package com.bsty.util;\n")
                        .append("import com.bsty.arouter.ARouter;\n")
                        .append("import com.bsty.arouter.IRouter;\n")
                        .append("\n")
                        .append("public class " + activityName + " implements IRouter {\n")
                        .append("    @Override\n")
                        .append("    public void putActivity() {\n");
                Iterator<String> iterator = map.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    String className = map.get(key);
                    stringBuffer.append("ARouter.getInstance().addActivity(\"" + key + "\"," + className + ");\n");
                }
                stringBuffer.append("\n}\n}");
                writer.write(stringBuffer.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }
}
