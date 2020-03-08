package com.dongduo.library.inventory.service;

import RFID.rfid_def;
import RFID.rfidlib_AIP_ISO18000P6C;
import RFID.rfidlib_reader;


public class MainFrm {
    public static long hReader = 0;

    public MainFrm() {
		loadLibrary();
	}

    public void connect() {
        Long hrOut = new Long(0);
        int nret = rfidlib_reader.RDR_Open("RDType=UHF_RPAN;CommType=USB;AddrMode=0;SerNum=", hrOut);
        if (nret != 0) {
            //TODO
            return;
        }
        hReader = hrOut;
    }

	public void getRecordPro() {
		int iret = 0;
		byte gFlg = 0x00;//
		long dnhReport = 0L;
		boolean b_threadRun = true;
		while (b_threadRun) {
			iret = rfidlib_reader.RDR_BuffMode_FetchRecords(hReader, gFlg); // send command to device
			if (iret != 0) {
				gFlg = 0x00;  // if fail ,try to get again.
				continue;
			}

			// Get records from dll buffer memory
			byte seekFirst = 1;
			byte seekNext = 2;
			dnhReport = rfidlib_reader.RDR_GetTagDataReport(hReader, seekFirst);
			while (dnhReport != 0) {
				String strData = "";
				byte[] byData = new byte[32];
				Integer len = new Integer(32);
				if (rfidlib_reader.RDR_ParseTagDataReportRaw(dnhReport, byData, len) == 0) {
					if (len > 0) {
						strData = byteToHex(byData).substring(0, 36);
						System.out.println("++++++" + strData);
					}
				}

				dnhReport = rfidlib_reader.RDR_GetTagDataReport(hReader, rfid_def.RFID_SEEK_NEXT); // next

			}
			gFlg = 0x01;  // if received ok ,get next records from device

		}
	}

	/**
	 * byte数组转hex
	 * @param bytes
	 * @return
	 */
	public static String byteToHex(byte[] bytes){
		String strHex = "";
		StringBuilder sb = new StringBuilder("");
		for (int n = 0; n < bytes.length; n++) {
			strHex = Integer.toHexString(bytes[n] & 0xFF);
			sb.append((strHex.length() == 1) ? "0" + strHex : strHex); // 每个字节由两个字符表示，位数不够，高位补0
		}
		return sb.toString().trim();
	}

    private void loadLibrary() {
        int osType = 0;
        int arType = 0;
        //String libPath = System.getProperty("user.dir");
		String libPath = "D:\\IdeaProjects\\inventory\\src\\main\\resources";
        String osName = System.getProperty("os.name");
        String architecture = System.getProperty("os.arch");
        osName = osName.toUpperCase();
        if (osName.equals("LINUX")) {
            osType = rfid_def.VER_LINUX;
        } else if (osName.indexOf("WIN") != -1) {
            osType = rfid_def.VER_WINDOWS;
        }

        architecture = architecture.toUpperCase();
        if (architecture.equals("AMD64") || architecture.equals("X64")
                || architecture.equals("UNIVERSAL")) {
            arType = rfid_def.AR_X64;
        } else {
            arType = rfid_def.AR_X86;
        }

        rfidlib_reader.LoadLib(libPath, osType, arType);
        rfidlib_AIP_ISO18000P6C.LoadLib(libPath, osType, arType);
    }
}
