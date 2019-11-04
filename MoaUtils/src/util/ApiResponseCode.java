package com.moa.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.ui.ModelMap;

public class ApiResponseCode {
	
	public static class StateCode{
		public static int SUCCESS=200;
		public static int ERROR=500;
	}
	
	public static enum DetailErrenum{
		
		ERR_INVALID_PARAMS("invalidParams", 500499),
		
		ERR_TELBAD_DOMAIN("telBAD_DOMAIN", 500399),
		ERR_TELBAD_PATH_COUNT("telBAD_PATH_COUNT", 500398),
		ERR_TELBAD_PUBLIC_KEY("telBAD_PUBLIC_KEY", 500397),
		ERR_TELCAN_NOT_QUEUE("telCAN_NOT_QUEUE", 500396),
		ERR_TELCAN_NOT_QUEUE_BALANCE("telCAN_NOT_QUEUE_BALANCE", 500395),
		ERR_TELCAN_NOT_QUEUE_BLOCKS("telCAN_NOT_QUEUE_BLOCKS", 500394),
		ERR_TELCAN_NOT_QUEUE_FEE("telCAN_NOT_QUEUE_FEE", 500393),
		ERR_TELFAILED_PROCESSING("telFAILED_PROCESSING", 500392),
		ERR_TELINSUF_FEE_P("telINSUF_FEE_P", 500391),
		ERR_TELLOCAL_ERROR("telLOCAL_ERROR", 500390),
		ERR_TELNO_DST_PARTIAL("telNO_DST_PARTIAL", 500389),
		
		
		ERR_TEMBAD_AMOUNT("temBAD_AMOUNT", 500299),
		ERR_TEMBAD_AUTH_MASTER("temBAD_AUTH_MASTER", 500298),
		ERR_TEMBAD_CURRENCY("temBAD_CURRENCY", 500297),
		ERR_TEMBAD_EXPIRATION("temBAD_EXPIRATION", 500296),
		ERR_TEMBAD_FEE("temBAD_FEE", 500295),
		ERR_TEMBAD_ISSUER("temBAD_ISSUER", 500294),
		ERR_TEMBAD_LIMIT("temBAD_LIMIT", 500293),
		ERR_TEMBAD_OFFER("temBAD_OFFER", 500292),
		ERR_TEMBAD_PATH("temBAD_PATH", 500291),
		ERR_TEMBAD_PATH_LOOP("temBAD_PATH_LOOP", 500290),
		ERR_TEMBAD_SEND_XRP_LIMIT("temBAD_SEND_XRP_LIMIT", 500289),
		ERR_TEMBAD_SEND_XRP_MAX("temBAD_SEND_XRP_MAX", 500288),
		ERR_TEMBAD_SEND_XRP_NO_DIRECT("temBAD_SEND_XRP_NO_DIRECT", 500287),
		ERR_TEMBAD_SEND_XRP_PARTIAL("temBAD_SEND_XRP_PARTIAL", 500286),
		ERR_TEMBAD_SEND_XRP_PATHS("temBAD_SEND_XRP_PATHS", 500285),
		ERR_TEMBAD_SEQUENCE("temBAD_SEQUENCE", 500284),
		ERR_TEMBAD_SIGNATURE("temBAD_SIGNATURE", 500283),
		ERR_TEMBAD_SRC_ACCOUNT("temBAD_SRC_ACCOUNT", 500282),
		ERR_TEMBAD_TRANSFER_RATE("temBAD_TRANSFER_RATE", 500281),
		ERR_TEMDST_IS_SRC("temTEMDST_IS_SRC", 500280),
		ERR_TEMDST_NEEDED("temTEMDST_NEEDED", 500279),
		ERR_TEMINVALID("temINVALID", 500278),
		ERR_TEMINVALID_FLAG("temINVLALID_FLAG", 500277),
		ERR_TEMMALFORMED("temMALFORMED", 500276),
		ERR_TEMREDUNDANT("temREDUNDANT", 500275),
		ERR_TEMREDUNDANT_SEND_MAX("temREDUNDANT_SEND_MAX", 500274),
		ERR_TEMRIPPLE_EMPTY("temRIPPLE_EMPTY", 500273),
		ERR_TEMBAD_WEIGHT("temBAD_WEIGHT", 500272),
		ERR_TEMBAD_SIGNER("temBAD_SIGNER", 500271),
		ERR_TEMBAD_QUORUM("temBAD_QUORUM", 500270),
		ERR_TEMUNCERTAIN("temUNCERTAIN", 500269),
		ERR_TEMUNKNOWN("temUNKNOWN", 500268),
		ERR_TEMDISABLED("temDISABLED", 500267),
		
		
		ERR_TEFALREADY("tefALREADY", 500199),
		ERR_TEFBAD_ADD_AUTH("tefBAD_ADD_AUTH", 500198),
		ERR_TEFBAD_AUTH("tefBAD_AUTH", 500197),
		ERR_TEFBAD_AUTH_MASTER("tefBAD_AUTH_MASTER", 500196),
		ERR_TEFBAD_LEDGER("tefBAD_LEDGER", 500195),
		ERR_TEFBAD_QUORUM("tefBAD_QUORUM", 500194),
		ERR_TEFBAD_SIGNATURE("tefBAD_SIGNATURE", 500193),
		ERR_TEFCREATED("tefCREATED", 500192),
		ERR_TEFEXCEPTION("tefEXCEPTION", 500191),
		ERR_TEFFAILURE("tefFAILURE", 500190),
		ERR_TEFINTERNAL("tefINTERNAL", 500189),
		ERR_TEFINVARIANT_FAILED("tefINVARIANT_FAILED", 500188),
		ERR_TEFMASTER_DISABLED("tefMASTER_DISABLED", 500187),
		ERR_TEFMAX_LEDGER("tefMAX_LEDGER", 500186),
		ERR_TEFNO_AUTH_REQUIRED("tefNO_AUTH_REQUIRED", 500185),
		ERR_TEFNOT_MULTI_SIGNING("tefNOT_MULTI_SIGNING", 500184),
		ERR_TEFPAST_SEQ("tefPAST_SEQ", 500183),
		ERR_TEFWRONG_PRIOR("tefWRONG_PRIOR", 500182),
		
		
		ERR_TERFUNDS_SPENT("terFUNDS_SPENT", 500099),
		ERR_TERINSUF_FEE_B("terINSUF_FEE_B", 500098),
		ERR_TERLAST("terLAST", 500097),
		ERR_TERNO_ACCOUNT("terNO_ACCOUNT", 500096),
		ERR_TERNO_AUTH("terNO_AUTH", 500095),
		ERR_TERNO_LINE("terNO_LINE", 500094),
		ERR_TERNO_RIPPLE("terNO_RIPPLE", 500093),
		ERR_TEROWNERS("terOWNERS", 500092),
		ERR_TERPRE_SEQ("terPRE_SEQ", 500091),
		ERR_TERRETRY("terRETRY", 500090),
		ERR_TERQUEUED("terQUEUED", 500089),
		
		
		ERR_UNKNOWN("Unknown Error", 500000),
		ERR_NOT_LOGIN("Not Login", 500500),
		ERR_NOT_LOGIN_WRONG_PWD("Not Login Wrong pwd", 500501),
		ERR_NOT_LOGIN_NOT_FOUND_USER("Not Login Not found user", 500502),
		
		ERR_RDB("rdb error", 500503),
		ERR_NODE("node error", 500504)
		;
	
		
		private String errname;
		private int errcode;
		
		
		DetailErrenum() {
			
		}
		
		DetailErrenum(String errName, int errcode) {
			this.errname = errName;
			this.errcode = errcode;
		}
		
		public String getErrName() {
			return errname;
		}
		
		public int getErrCode() {
			return errcode;
		}
		
		public static boolean hasErrorCode(int errcode) {
			for (DetailErrenum e : DetailErrenum.values()) {
				if (e.getErrCode() == errcode)
					return true;
			}
			return false;
		}
		
		public static DetailErrenum fromErrorCode(int errcode) {
			for (DetailErrenum e : DetailErrenum.values()) {
				if (e.getErrCode() == errcode)
					return e;
			}
			return null;
		}
		
		public static DetailErrenum fromString(String errName) {
			for (DetailErrenum e : DetailErrenum.values()) {
				if (e.getErrName().compareTo(errName) == 0)
					return e;
			}
			return null;
		}
	}
	
	
	
	public static class DetailCode{
		public static int SUCCESS=200;
		public static int OVERLAP=201; //중복
		public static int NOTVALUE=2011; //조회결과 없음
		public static int EMAILCERTIFICATION=508; //이메일 인증 진행중
		
		public static int ERR_UN_KNOWN=500000;
		
		public static int NOT_LOGIN=500500;
		public static int NOT_LOGIN_WRONG_PWD=500501;
		public static int NOT_LOGIN_NOT_FOUND_USER=500502;
		
		public static int FEE_INSUFFICIENT=500602;
		public static int AMOUNT_INSUFFICIENT=500603;

		public static int ERR_RDB=500501;
		public static int ERR_NODE=500502;
		
		public static int ERR_NO_ACCOUNT = 500599;
		public static int ERR_INVALID_PARAM = 500598;
		
	
		
		/*
		These code indicate an error in the local sever processing in the transaction. It is 
		possible that another server with a different configuration or load level could process
		the transaction successfully. They have numerical values in the range -399 to -300. The
		exact code for any given error is subject to change, so don't rely on it.
		
		 */
	
		
		/*
		 These codes indicate that the transaction was malformed and cannot succeed according to
		 the XRP Ledger protocol. They have numerical values in the range -299 to -200. The exact
		 code for any given error is subject to change. so don't rely on it.
		 */
		
		
		
	}
	public static void makeResponse(ModelMap mv,int stateCode,int detailCode,Exception e) {
		mv.addAttribute("stateCode", stateCode);
		mv.addAttribute("detailCode", detailCode);
		if (DetailErrenum.hasErrorCode(detailCode))
			mv.addAttribute("errMsg", DetailErrenum.fromErrorCode(detailCode).getErrName());
		
		
		
		if(e!=null) {
			StringWriter sw=new StringWriter();
			PrintWriter pw=new PrintWriter(sw);
			e.printStackTrace(pw);
			mv.addAttribute("errMsg", sw.toString());
			mv.addAttribute("stateCode", StateCode.ERROR);
			mv.addAttribute("detailCode", DetailCode.ERR_UN_KNOWN);
		}
	}
	
	
}