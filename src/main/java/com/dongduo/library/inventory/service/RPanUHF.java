package com.dongduo.library.inventory.service;

import RFID.rfid_def;
import RFID.rfidlib_AIP_ISO18000P6C;
import RFID.rfidlib_reader;
import com.dongduo.library.inventory.util.EpcCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@ConditionalOnProperty(name = "inventory.useSimulatedDevice", havingValue = "false")
public class RPanUHF implements IRPanUHF{
	private static final Logger logger = LoggerFactory.getLogger(RPanUHF.class);

    public static long hReader = 0;

    public RPanUHF() {
		loadLibrary();
	}

	@Override
    public boolean connect() {
        Long hrOut = new Long(0);
        int nret = rfidlib_reader.RDR_Open("RDType=UHF_RPAN;CommType=USB;AddrMode=0;SerNum=", hrOut);
        if (nret != 0) {
            return false;
        }
        hReader = hrOut;
        return true;
    }

	@Override
    public void disconnect() {
		if (hReader == 0L) {
			return;
		}
		rfidlib_reader.RDR_Close(hReader);
		hReader = 0L;
	}

	@Override
	public Set<EpcCode> getRecordEpc() {
		byte gFlg = 0x00;//
		boolean b_threadRun = true;
		Set<EpcCode> epcSet = new HashSet<>();
		while (b_threadRun) {
			int iret = rfidlib_reader.RDR_BuffMode_FetchRecords(hReader, gFlg); // send command to device
			if (iret != 0) {
				gFlg = 0x00;  // if fail ,try to get again.
				continue;
			}

			// Get records from dll buffer memory
			long dnhReport = rfidlib_reader.RDR_GetTagDataReport(hReader, rfid_def.RFID_SEEK_FIRST);
			while (dnhReport != 0) {
				byte[] byData = new byte[32];
				Integer len = new Integer(32);
				if (rfidlib_reader.RDR_ParseTagDataReportRaw(dnhReport, byData, len) == 0) {
					if (len > 0) {
						String epcHex = byteToHex(byData).substring(0, 36);
						try {
							epcSet.add(EpcCode.parseEpcCode(epcHex));
						} catch (Exception e) {
							logger.error("EPC HEX \"" + epcHex + "\"解析失败。", e);
						}
					}
				}
				dnhReport = rfidlib_reader.RDR_GetTagDataReport(hReader, rfid_def.RFID_SEEK_NEXT); // next
			}
			gFlg = 0x01;  // if received ok ,get next records from device
		}
		return epcSet;
	}

	/**
	 * byte数组转hex
	 * @param bytes
	 * @return
	 */
	private String byteToHex(byte[] bytes){
		StringBuilder sb = new StringBuilder("");
		for (int n = 0; n < bytes.length; n++) {
			String strHex = Integer.toHexString(bytes[n] & 0xFF);
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
