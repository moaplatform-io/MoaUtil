package io.moa.bmt.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GenerateContractUtil {
	
	public static void main(String[] args) throws Exception {
		
		generateContractFile(args[0], Integer.parseInt(args[1]), args[2]);
		
	}
	
	private static void generateContractFile(String filePath, int generateNum, String genType) throws SecurityException, IOException {
		// writes contents to file
		BufferedWriter bufferedExecWriter = null;
		BufferedWriter bufferedQueryWriter = null;

		try{
			
			if(genType.equals("BLC")) {
				
				bufferedExecWriter = new BufferedWriter(new FileWriter(filePath+"_exec.M", false));
				bufferedQueryWriter = new BufferedWriter(new FileWriter(filePath+"_query.M", false));

				String affiliatesCode = "001";
				String regDate = "2019113349";
				String notifyCode = "6";
				String lastModeifyTypeCode = "1";
				String publicKey = "fe20c302f7084e1f72311e1b14cc3413aa6d5c3dec2504e770fc0be73b501baa";
				String pinPwd = "2c605365ebb17226ba037d90dd9913d2a962d8ccda214826082a87e3f1efcd8a";
				
				
				for(int i=1;i<=generateNum;i++) {
					
					String icid = Sha256.sha256(i+"");
					String ci = Sha256.sha256(i+""+i);
					
					String contractExecBody = "call(\"toolbmtSaveCert\""
										  + ",\""+ icid +"\""
										  + ",\""+ affiliatesCode +"\""									  
										  + ",\""+ ci +"\""
										  + ",\""+ regDate +"\""
										  + ",\""+ pinPwd +"\""
										  + ",\""+ notifyCode +"\""
										  + ",\""+ lastModeifyTypeCode +"\""
										  + ",\""+ publicKey +"\""
										  +")";
					
					
					bufferedExecWriter.write(contractExecBody + "\n");	
					
					String contractQueryBody = "ret=call(\"toolbmtSelectCert\""
											+ ",\""+ icid +"\""
											+ ",\""+ ci +"\""
											+ ",\""+ affiliatesCode +"\""		
							 				+");return ret;";
					
					bufferedQueryWriter.write(contractQueryBody + "\n");		
				}
				
			}else if(genType.equals("SH")) {
				
				bufferedExecWriter = new BufferedWriter(new FileWriter(filePath+"_exec.M", false));
				bufferedQueryWriter = new BufferedWriter(new FileWriter(filePath+"_query.M", false));
				
				String affiliatesCode = "001";
				String regDate = "20191111123349";
				String notifyCode = "6";
				String pinPwd = "2c605365ebb17226ba037d90dd9913d2a962d8ccda214826082a87e3f1efcd8a";
				String isAll = "true";
				
				for(int i=1;i<=generateNum;i++) {
					
					String icid = Sha256.sha256(i+"");
					String ci = Sha256.sha256(i+""+i);
					
					String info = "";
					
					String contractExecBody = "call(\"test\""
										  + ",\""+ icid +"\""
										  + ",\""+ affiliatesCode +"\""									  
										  + ",\""+ ci +"\""
										  + ",\""+ regDate +"\""
										  + ",\""+ pinPwd +"\""
										  + ",'"+ info +"'"
										  + ",\""+ notifyCode +"\""
										  +")";
					
					
					bufferedExecWriter.write(contractExecBody + "\n");	
					
					String contractQueryBody = "ret=call(\"testa\""
											+ ",\""+ icid +"\""
											+ ",\""+ ci +"\""
											+ ",\""+ affiliatesCode +"\""	
											+ ",\""+ isAll +"\""	
							 				+");return ret;";
					bufferedQueryWriter.write(contractQueryBody + "\n");		
				}
				
			}else {
				System.err.println(
						"Usage: java moa.bmt.bmt.util.GenerateContractUtil {FilePath} {GenerateOfCount} {GenerateType=BLC|SHIN}");
				throw new RuntimeException("Wrong to Argument the {GenerateType}");
			}
			
		} finally {
			bufferedExecWriter.close();
			bufferedQueryWriter.close();
		}
	}
	
}
