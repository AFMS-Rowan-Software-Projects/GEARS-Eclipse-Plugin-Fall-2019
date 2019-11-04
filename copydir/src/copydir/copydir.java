package copydir;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;

public class copydir 
{
	public static void main(String[] a) throws IOException
	{
		File f = new File("ex");
		
		project(f);
	}
	
	
	public static void project(File from) throws IOException
	{
		File to = new File(from.getName() + "_proj");
		to.mkdir();
		
		cloneDir(from, to);
	}
	
	public static void cloneDir(File from, File to) throws IOException
	{
		File[] files = from.listFiles();
		String pattern = "[.]+.";
		for(int i = 0; i < files.length; i++)
		{
			File temp = new File(to, files[i].getName());
			if(files[i].isDirectory())
			{
				if(Pattern.matches(pattern, files[i].getName()))	//if temp is projected directory, modify name and apply projection
				{
					temp = new File(to, temp.getName().substring(1));
					temp.mkdir();
					//TODO: apply projection
				}
				else	//else, create the directory and continue copying recursively
				{
					temp.mkdir();
					cloneDir(files[i], temp);
				}
			}
			else
				try
				{
					if(Pattern.matches(pattern, files[i].getName()))	//if temp is projected file, modify name and apply projection
					{
						temp = new File(to, temp.getName().substring(1));
						temp.createNewFile();
						//TODO: apply projection
					}
					else	//else, just copy the file as below
					{					
						temp.createNewFile();
						var source = Paths.get(files[i].toString());
				        var dest = Paths.get(temp.toString());
	
				        try (var fis = Files.newInputStream(source);
				             var fos = Files.newOutputStream(dest)) {
	
				            byte[] buffer = new byte[1024];
				            int length;
	
				            while ((length = fis.read(buffer)) > 0) {
	
				                fos.write(buffer, 0, length);
				            }
				        }
					}
				}
				catch (IOException e)
				{
				    e.printStackTrace();
				}
		}
	}

}
