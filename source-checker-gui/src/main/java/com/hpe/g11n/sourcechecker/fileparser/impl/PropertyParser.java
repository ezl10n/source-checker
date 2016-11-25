package com.hpe.g11n.sourcechecker.fileparser.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.hpe.g11n.sourcechecker.fileparser.BaseParser;
import com.hpe.g11n.sourcechecker.pojo.InputData;

/**
 * 
 * 
 * @Descripation
 * @CreatedBy: Ali Cao
 * @Date: 2016年10月9日
 * @Time: 上午11:10:08
 *
 */
public class PropertyParser extends BaseParser{

	InputData ido;
	List<InputData> lstIdo;
	private List<InputData> parser(String filePath,String scope){
		lstIdo = new ArrayList<InputData>();
		try {
			FileReader reader = new FileReader(filePath);
			BufferedReader br = new BufferedReader(reader);

			String str = null;
			while ((str = br.readLine()) != null) {
				if(!str.startsWith("#") && str.contains("=")){
					ido =  new InputData();
					String[] temp = str.split("=");
					ido.setStringId(temp[0]);
					ido.setSourceString(temp[1]);
					ido.setFileName(getName(filePath));
					ido.setFileVersion("");
					ido.setLpuName("");
					lstIdo.add(ido);
					ido = null;
				}
			}
			br.close();
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lstIdo;
	}
	@Override
	public boolean isHandle(String source) {
		return source.endsWith(".properties");
	}
	@Override
	public List<InputData> getInputData(String source,String state){
		return parser(source,state);
	}
	public String getName(String path) {
		int index = path.lastIndexOf("\\");
		return path.substring(index+1, path.length());
	}
}
