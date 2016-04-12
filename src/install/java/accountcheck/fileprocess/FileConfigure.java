package install.java.accountcheck.fileprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class FileConfigure {
	
	private File config;
	
	public FileConfigure(File config) {this.config = config;}
	
	public boolean setConfig(String path, boolean newValue) {
		
		BufferedReader br = null;
		BufferedWriter wr = null;
		
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(config), "UTF-8"));
			ArrayList<String> configDetail = new ArrayList<String>();
			
			String tmp;
			while((tmp = br.readLine()) != null)
				configDetail.add(tmp);
			
			String pathDetail[];
			for(int index=0; index<configDetail.size(); index++) {
				tmp = configDetail.get(index);
				if(tmp.charAt(0) == '#')
					continue;
				pathDetail = tmp.split(":");
				if(pathDetail[0].equals(path)) {
					configDetail.set(index, path + ": " + newValue);
					break;
				}
			}
			
			br.close();
			
			wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(config), "UTF-8"));
			for(String str:configDetail) {
				wr.write(str);
				wr.newLine();
			}
			
			wr.close();
			return true;
		}catch(UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}catch(FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}catch(IOException e) {
			e.printStackTrace();
			return false;
		}finally {
			try{
				if(br!=null)
					br.close();
				if(wr!=null)
					wr.close();
			}catch(IOException e){
				e.printStackTrace();
				return false;
			}
		}
	}
	
	public boolean setConfig(String path, String newValue) {
		
		BufferedReader br = null;
		BufferedWriter wr = null;
		
		try {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(config), "UTF-8"));
				ArrayList<String> configDetail = new ArrayList<String>();
				
				String tmp;
				while((tmp = br.readLine()) != null)
					configDetail.add(tmp);
				
				String pathDetail[];
				for(int index=0; index<configDetail.size(); index++) {
					tmp = configDetail.get(index);
					if(tmp.charAt(0) == '#')
						continue;
					pathDetail = tmp.split(":");
					if(pathDetail[0].equals(path)) {
						configDetail.set(index, path + ": " + "'" + newValue + "'");
						break;
					}
				}
				
				br.close();
				
				wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(config), "UTF-8"));
				for(String str:configDetail) {
					wr.write(str);
					wr.newLine();
				}
				
				wr.close();
				return true;
		}catch(UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}catch(FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}catch(IOException e) {
			e.printStackTrace();
			return false;
		}finally {
			try{
				if(br!=null)
					br.close();
				if(wr!=null)
					wr.close();
			}catch(IOException e){
				e.printStackTrace();
				return false;
			}
		}
	}
	
}
