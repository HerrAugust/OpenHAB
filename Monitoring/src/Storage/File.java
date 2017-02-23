package Storage;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * Based on java.nio.File, it makes easier to read/write from file.
 * Example: File f = new File("ciao.txt");
 * 			List<String> s = f.readAll();
 * 			f.write("lorem ipsus");
 * @author agost
 *
 */
public class File {
	private String relPath;

	public File(String relPath) {
		this.relPath = relPath;
	}
	
	/**
	 * Reads all from file, returning lines as List<String>
	 * @return entire content of file
	 */
	public List<String> readAll() {
		Path path;
		List<String> res = null;
		try {
			path = Paths.get(relPath);
			res = Files.readAllLines(path, Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(res == null)
			res = new LinkedList<String>();
		return res;
	}
	
	/**
	 * Writes on file, creating if not existing and truncating it
	 * @param what string to write
	 * @return true on success
	 */
	public boolean write(String what) {
		try {
		Path path = Paths.get(relPath);
		Files.write(path, what.getBytes());
		} catch(IOException e) { return false; }
		return true;
	}
}
