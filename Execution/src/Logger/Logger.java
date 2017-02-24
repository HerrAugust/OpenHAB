package Logger;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;

import javax.swing.JOptionPane;


public class Logger {
	
	private String dir_path = System.getProperty("user.dir");
	private String filename = "log.txt";
	private String context = "";
	
	public Logger(String folder, String filename) {
		this.setLoggingFile(folder, filename);
	}
	
	public Logger(String context) {
		this.context = context;
	}

	public void setLoggingFile(String folder, String filename) {
		this.dir_path = folder;
		this.filename = filename;
	}
	
	/**
	 * Write a log on a file, inserting newline
	 * @param line what to write on file
	 */
	public void writeln(String log) {
		LinkedList<String> t = new LinkedList<String>();
		t.add(log);
		this.writelogs(t);
	}
	
	/**
	 * Writes logs on a file, inserting newlines
	 * @param lines what to write on file
	 */
	public void writelogs(LinkedList<String> lines){
		//FileLock lock = null;
		try {
			String path= dir_path + File.separator + filename;
			URI completePath = new File(path).toURI();
			Path file = Paths.get(completePath);
			file.toFile().createNewFile();
			//FileInputStream in = new FileInputStream(path);
			//lock = in.getChannel().lock();
			lines.addFirst(this.context.toUpperCase());
			Files.write(file, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
			//in.close();
			//lock.release();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error wrinting goals file", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	} 
}
