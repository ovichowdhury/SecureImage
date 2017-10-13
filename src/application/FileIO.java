package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import javax.swing.JOptionPane;

import org.apache.commons.io.FilenameUtils;

import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FileIO {
	
	static int key;
	private static File file;
	private static File choosenFile;
	
	static File tempFile;
	
	private FileChooser chooser;
	private FileChooser chooser2;
	private FileInputStream in;
	private FileOutputStream out;
	private byte [] data;
	private byte [] tempData;
	boolean fileEncrypted=false;
	
	public FileIO(){
		chooser = new FileChooser();
		chooser2 = new FileChooser();
	}
	
	public static int createKey(){
		int key=2;
		try{
			FileInputStream input = new FileInputStream("key.dat");
			try {
				key = input.read();
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}catch(FileNotFoundException fEx){
			File file = new File("key.dat");
			try(FileOutputStream out = new FileOutputStream(file)){
				Random rand = new Random();
				//key = (int) (rand.nextDouble() * 100) + 1;
				key = rand.nextInt(254) + 1;
				out.write(key);
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		return key;
	}
	
	
	
	
	public static byte[] encryptData(byte[] array){
		int i=0;
		int j=array.length-1;
		
		while(i <= j){
			byte temp = array[i];
			array[i] = array[j];
			array[j] = temp;
			i++;
			j--;
		}
		
		for(int index=0; index<array.length; index++){
			array[index] = (byte)((byte) array[index] - key);
		}
		
		return array;
	}
	
	public static  byte[] decryptData(byte[] array){
		int i=0;
		int j=array.length-1;
		
		while(i <= j){
			byte temp = array[i];
			array[i] = array[j];
			array[j] = temp;
			i++;
			j--;
		}
		
		for(int index=0; index<array.length; index++){
			array[index] = (byte)((byte) array[index] + key);
		}
		
		return array;
	}
	
	public  void openNewFile(Stage primaryStage,ImageView container,Label lbl,MenuItem encrypt,MenuItem decrypt){
		try{
			file = chooser.showOpenDialog(primaryStage);
			String exten = FilenameUtils.getExtension(file.getAbsolutePath());
			if(exten.equals("so") || exten.equals("SO")){
				this.createTempImage();
				container.setImage(new Image("file:temp.jpg"));
				lbl.setText(file.getName());
				encrypt.setDisable(true);
				decrypt.setDisable(false);
				data = null;
			}
			
			if(exten.equals("jpg") || exten.equals("JPG") || exten.equals("png")){
				String str = "file:" +file.getAbsolutePath();
				container.setImage(new Image(str));
				System.out.println(str);
				lbl.setText(file.getName());
				encrypt.setDisable(false);
				decrypt.setDisable(true);
				this.flushData();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void flushData() {
		data = null;
		fileEncrypted = false;
	}

	public void encryptImage(MenuItem item) throws Exception{
		FileIO.key = FileIO.createKey();
		//System.out.println(FileIO.key);
		
		try{
			if(fileEncrypted == false){
				in = new FileInputStream(file);
				data = new  byte [in.available()];
				in.read(data);
				data = FileIO.encryptData(data);
				item.setDisable(true);
				fileEncrypted = true;
				
				in.close();
			}
		}catch(Exception ex){
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "File Not Found");
			throw ex;
		}
	}
	
	public void decryptAndSaveImage(){
		try{
			String baseName = FilenameUtils.getBaseName(file.getName());
			out = new FileOutputStream(baseName +".jpg");
			out.write(tempData);
			
			
		}catch(Exception ex){
			JOptionPane.showMessageDialog(null, "ERROR DECRYPTING FILE");
		}finally{
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	public void saveImage(){
		try{
			if(data != null){
				String baseName = FilenameUtils.getBaseName(file.getName());
				System.out.println(baseName);
				out = new FileOutputStream( baseName + ".so");
				out.write(data);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "ERROR SAVING FILE");
		}finally{
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void saveAsImage(Stage stage){
		try{
			if(data != null){
				FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("SECURE OBJECT", "*.so");
				chooser2.getExtensionFilters().add(filter);
				choosenFile = chooser2.showSaveDialog(stage);
				
				out = new FileOutputStream(choosenFile);
				out.write(data);
			}
			
		}catch(Exception ex){
			JOptionPane.showMessageDialog(null, "NO ENCRYPTED FILE FOUND");
		}finally{
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void createTempImage(){
		try{
			in = new FileInputStream(file);
			tempData = new byte[in.available()];
			in.read(tempData);
			
			//FileIO.key = FileIO.createKey();
			tempData = FileIO.decryptData(tempData);
			
			out = new FileOutputStream("temp.jpg");
			out.write(tempData);
			
			tempFile = new File("temp.jpg");
			
			in.close();
			out.close();
		}catch(Exception ex){
			JOptionPane.showMessageDialog(null, "ERROR OPENING FILE");
		}
	}

}
