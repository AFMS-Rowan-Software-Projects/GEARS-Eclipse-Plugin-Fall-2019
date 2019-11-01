package copydir;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class copydir 
{
	public static void main(String[] a) throws IOException
	{
		File f = new File("ex");
		File n = new File("newex");
		n.mkdir();
		
		cloneDir(f, n);
	}
	
	public static void cloneDir(File from, File to) throws IOException
	{
		File[] files = from.listFiles();
		for(int i = 0; i < files.length; i++)
		{
			File temp = new File(to, files[i].getName());
			if(files[i].isDirectory())
			{
				//TODO: if temp is projected directory, modify name and apply projection
				//else, create the directory and continue copying recursively
				temp.mkdir();
				cloneDir(files[i], temp);
			}
			else
				try
				{
					//TODO: if temp is projected file, modify name and apply projection
					//else, just copy the file as below
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
				catch (IOException e)
				{
				    e.printStackTrace();
				}
		}
	}

}
