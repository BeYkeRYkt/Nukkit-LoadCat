package LoadCat;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.JavaPluginLoader;
import java.io.*;
import java.util.zip.*;
import java.util.jar.*;

public class LoadCat extends PluginBase {
	public String[] plugin = new String[1024];
	public String[] ymlname = new String[1024];
	public int plugins = 0;
	public String[] nplugin = new String[1024];
	public String[] nymlname = new String[1024];
	public int nplugins = 0;
	public String[] lfile = new String[1024];
	public int lfiles = 0;
	JarOutputStream target;
	
	public boolean deleteDirectory(File directory) {
		if(directory.exists()){
			File[] files = directory.listFiles();
			if(null!=files){
				for(int i=0; i<files.length; i++) {
					if(files[i].isDirectory()) {
						deleteDirectory(files[i]);
					}
					else {
						files[i].delete();
					}
				}
			}
		}
		return(directory.delete());
	}
	
	public void onEnable(){
		nplugins = 0;
		plugins = 0;
		this.getDataFolder().mkdirs();
		File[] listOfFiles = new File("plugins").listFiles();
		for (File file : listOfFiles) {
			if (file.isDirectory()) {
				if(new File("plugins/"+file.getName()+"/plugin.yml").exists()){
					String pluginyml = readFile("plugins/"+file.getName()+"/plugin.yml");
					if(pluginyml.contains("name:") && pluginyml.contains("main:") && pluginyml.contains("version:") && pluginyml.contains("api:")){
						if(!new File("plugins/"+file.getName()+".jar").exists()){
							plugin[plugins] = file.getName();
							if(pluginyml.contains("name: ")){
								ymlname[plugins] = pluginyml.split("name: ")[1].split("\n")[0];
							}
							else
							{
								ymlname[plugins] = pluginyml.split("name:")[1].split("\n")[0];
							}
							plugins++;
						}
					}
				}
				else if(new File("plugins/"+file.getName()+"/src/main/resources/plugin.yml").exists()){
					String pluginyml = readFile("plugins/"+file.getName()+"/src/main/resources/plugin.yml");
					if(pluginyml.contains("name:") && pluginyml.contains("main:") && pluginyml.contains("version:") && pluginyml.contains("api:")){
						if(!new File("plugins/"+file.getName()+".jar").exists()){
							nplugin[nplugins] = file.getName();
							if(pluginyml.contains("name: ")){
								nymlname[nplugins] = pluginyml.split("name: ")[1].split("\n")[0];
							}
							else
							{
								nymlname[nplugins] = pluginyml.split("name:")[1].split("\n")[0];
							}
							nplugins++;
						}
					}
				}
			}
		}
		for(int i = 0; i<plugins; i++){
			nplugins = 0;
			plugins = 0;
			this.getLogger().notice("Обнаружен исходный код плагина "+ymlname[i]+".");
			this.getLogger().notice("Загрузка плагина "+ymlname[i]+"...");
			final int isl = i;
			if(new File("nukkit.jar").exists()){
				try {
					Process proc = Runtime.getRuntime().exec("javac -classpath nukkit.jar -sourcepath ./plugins/"+plugin[i]+" -d plugins/LoadCat plugins/"+plugin[i]+"/"+readFile("plugins/"+plugin[i]+"/plugin.yml").split("main:")[1].split("\n")[0].replace(" ","").replace(".","/")+".java");                        
					proc.waitFor();
					target = new JarOutputStream(new FileOutputStream("plugins/"+plugin[i]+".jar"));
					add(new File("plugins/"+plugin[i],"plugin.yml"), new File("plugin.yml"), target);
					lfiles = 0;
					listFilesForFolder(new File("plugins/LoadCat/"+plugin[i]));
					this.getServer().getScheduler().scheduleDelayedTask(new Runnable() {
							public void run() {
								for(int isub = 0; isub<lfiles; isub++){
									addTarg(new File("plugins/LoadCat",lfile[isub]), new File(lfile[isub]));
								}
								closeTarget();
								deleteDirectory(new File("plugins/LoadCat",plugin[isl].split("/")[0]));
							}
						},10);
					this.getServer().getScheduler().scheduleDelayedTask(new Runnable() {
							public void run() {
								reloadServer(plugin[isl]);
							}
						},20);
				} catch(Exception e){}
			} else if(new File("nukkit-1.0-SNAPSHOT.jar").exists()){
				try {
					Process proc = Runtime.getRuntime().exec("javac -classpath nukkit-1.0-SNAPSHOT.jar -sourcepath ./plugins/"+plugin[i]+" -d plugins/LoadCat plugins/"+plugin[i]+"/"+readFile("plugins/"+plugin[i]+"/plugin.yml").split("main:")[1].split("\n")[0].replace(" ","").replace(".","/")+".java");                        
					proc.waitFor();
					target = new JarOutputStream(new FileOutputStream("plugins/"+plugin[i]+".jar"));
					add(new File("plugins/"+plugin[i],"plugin.yml"), new File("plugin.yml"), target);
					lfiles = 0;
					listFilesForFolder(new File("plugins/LoadCat/"+plugin[i]));
					this.getServer().getScheduler().scheduleDelayedTask(new Runnable() {
							public void run() {
								for(int isub = 0; isub<lfiles; isub++){
									addTarg(new File("plugins/LoadCat",lfile[isub]), new File(lfile[isub]));
								}
								closeTarget();
								deleteDirectory(new File("plugins/LoadCat",plugin[isl].split("/")[0]));
							}
						},10);
					this.getServer().getScheduler().scheduleDelayedTask(new Runnable() {
							public void run() {
								reloadServer(plugin[isl]);
							}
						},20);
				} catch(Exception e){}
			} else {
				this.getLogger().error("Ошибка при получении ядра. Пожалуйста, переименуйте ядро в nukkit.jar и повторите попытку.");
			}
		}
		for(int i = 0; i<nplugins; i++){
			nplugins = 0;
			plugins = 0;
			this.getLogger().notice("Обнаружен исходный код плагина "+nymlname[i]+".");
			this.getLogger().notice("Загрузка плагина "+nymlname[i]+"...");
			final int isl = i;
			if(new File("nukkit.jar").exists()){
				try {
					Process proc = Runtime.getRuntime().exec("javac -classpath nukkit.jar -sourcepath ./plugins/"+nplugin[i]+" -d plugins/LoadCat plugins/"+nplugin[i]+"/src/main/java/"+readFile("plugins/"+nplugin[i]+"/src/main/resources/plugin.yml").split("main:")[1].split("\n")[0].replace(" ","").replace(".","/")+".java");                        
					proc.waitFor();
					target = new JarOutputStream(new FileOutputStream("plugins/"+nplugin[i]+".jar"));
					add(new File("plugins/"+nplugin[i],"plugin.yml"), new File("plugin.yml"), target);
					lfiles = 0;
					listFilesForFolder(new File("plugins/LoadCat/"+nplugin[i]));
					this.getServer().getScheduler().scheduleDelayedTask(new Runnable() {
							public void run() {
								for(int isub = 0; isub<lfiles; isub++){
									addTarg(new File("plugins/LoadCat",lfile[isub]), new File(lfile[isub]));
								}
								closeTarget();
								deleteDirectory(new File("plugins/LoadCat",plugin[isl].split("/")[0]));
							}
						},10);
					this.getServer().getScheduler().scheduleDelayedTask(new Runnable() {
							public void run() {
								reloadServer(plugin[isl]);
							}
						},20);
				} catch(Exception e){}
			} else if(new File("nukkit-1.0-SNAPSHOT.jar").exists()){
				try {
					Process proc = Runtime.getRuntime().exec("javac -classpath nukkit-1.0-SNAPSHOT.jar -sourcepath ./plugins/"+nplugin[i]+" -d plugins/LoadCat plugins/"+nplugin[i]+"/src/main/java/"+readFile("plugins/"+nplugin[i]+"/src/main/resources/plugin.yml").split("main:")[1].split("\n")[0].replace(" ","").replace(".","/")+".java");                        
					proc.waitFor();
					target = new JarOutputStream(new FileOutputStream("plugins/"+nplugin[i]+".jar"));
					add(new File("plugins/"+nplugin[i],"plugin.yml"), new File("plugin.yml"), target);
					listFilesForFolder(new File("plugins/LoadCat/"+nplugin[i]));
					lfiles = 0;
					this.getServer().getScheduler().scheduleDelayedTask(new Runnable() {
							public void run() {
								for(int isub = 0; isub<lfiles; isub++){
									addTarg(new File("plugins/LoadCat",lfile[isub]), new File(lfile[isub]));
								}
								closeTarget();
								deleteDirectory(new File("plugins/LoadCat",plugin[isl].split("/")[0]));
							}
						},10);
					this.getServer().getScheduler().scheduleDelayedTask(new Runnable() {
							public void run() {
								reloadServer(plugin[isl]);
							}
						},20);
				} catch(Exception e){}
			} else {
				this.getLogger().error("Ошибка при получении ядра. Пожалуйста, переименуйте ядро в nukkit.jar и повторите попытку.");
			}
		}
	}

	public void addTarg(File one, File two){
		try {
			add(one, two, target);
		} catch(Exception e){}
	}

	public void closeTarget(){
		try {
			target.close();
		} catch(Exception e){}
	}

	public void reloadServer(String pn){
		try {
			this.getServer().reload();
		} catch(Exception e){}
	}

	public void listFilesForFolder(final File folder) {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				try {
					lfile[lfiles] = fileEntry.getCanonicalPath().split("LoadCat/")[1];
					lfiles++;
				} catch(Exception e){}
			}
		}
	}

	private void add(File source2, File source, JarOutputStream target) throws IOException
	{
		BufferedInputStream in = null;
		try
		{
			if (source.isDirectory())
			{
				String name = source.getPath().replace("\\", "/");
				if (!name.isEmpty())
				{
					if (!name.endsWith("/"))
						name += "/";
					JarEntry entry = new JarEntry(name);
					entry.setTime(source.lastModified());
					target.putNextEntry(entry);
					target.closeEntry();
				}
				for (File nestedFile: source.listFiles())
					add(source2, nestedFile, target);
				return;
			}

			JarEntry entry = new JarEntry(source.getPath().replace("\\", "/"));
			entry.setTime(source.lastModified());
			target.putNextEntry(entry);
			in = new BufferedInputStream(new FileInputStream(source2));

			byte[] buffer = new byte[1024];
			while (true)
			{
				int count = in.read(buffer);
				if (count == -1)
					break;
				target.write(buffer, 0, count);
			}
			target.closeEntry();
		}
		finally
		{
			if (in != null)
				in.close();
		}
	}

	private String readFile(String file){
		String stc = "null";
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(file)));
			try {
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();
				while (line != null) {
					sb.append(line);
					sb.append(System.lineSeparator());
					line = br.readLine();
				}
				stc = sb.toString();
			} finally {
				br.close();
			}
		} catch(Exception e){}
		return stc;
	}
}
