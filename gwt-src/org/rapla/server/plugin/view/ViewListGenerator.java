package org.rapla.server.plugin.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class ViewListGenerator extends Generator {
	@Override
	public String generate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {
		JClassType classType;
		try {
			classType = context.getTypeOracle().getType(typeName);
			Enumeration<URL> resources = getClass()
					.getClassLoader()
					.getResources(
							"META-INF/org.rapla.client.plugin.view.ViewPlugins");
			List<String> plugins = new ArrayList<>();
			while (resources.hasMoreElements()) {
				URL nextElement = resources.nextElement();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(nextElement.openStream()));
				String clazz = reader.readLine();
				while (clazz != null) {
					if (!plugins.contains(clazz))
						plugins.add(clazz);
					clazz = reader.readLine();
				}
				reader.close();
			}
			SourceWriter src = getSourceWriter(classType, context, logger);
			if (src == null)
				return null;
			src.println("public List<ViewPlugin> getViews() {");
			src.indent();
			src.println("List<ViewPlugin> plugins = new ArrayList<>();");
			for (String plugin : plugins) {
				src.println("plugins.add(new " + plugin + "());");
			}
			src.println("return plugins;");
			src.outdent();
			src.println("}");
			src.commit(logger);

			System.out.println("Generating for: " + typeName);
			return typeName + "Generated";

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public SourceWriter getSourceWriter(JClassType classType,
			GeneratorContext context, TreeLogger logger) {
		String packageName = classType.getPackage().getName();
		String simpleName = classType.getSimpleSourceName() + "Generated";
		ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(
				packageName, simpleName);
		composer.setSuperclass(classType.getName());
		// Need to add whatever imports your generated class needs.
		composer.addImport("java.util.List");
		composer.addImport("java.util.ArrayList");
		composer.addImport("org.rapla.client.plugin.view.ViewPlugin");
		PrintWriter printWriter = context.tryCreate(logger, packageName,
				simpleName);
		if (printWriter == null) {
			return null;
		} else {
			SourceWriter sw = composer.createSourceWriter(context, printWriter);
			return sw;
		}
	}
}
