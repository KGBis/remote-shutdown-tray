package com.kikegg.remote.shutdown;

import lombok.Getter;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;

@Log
public class CMDWriter {

    @Getter
    private int time = 1;

    public CMDWriter(String timeParam) {
        // Client MAY send no time unit if no unit radio button is selected in mobile app -> 0null
        String nullFreeTimeParam = StringUtils.replace(StringUtils.trim(timeParam), "null", "s");

        // Trim and extract time suffix ("s" for seconds, "m" for minutes or "h" for hours)
        String timeInString = StringUtils.lowerCase(StringUtils.substring(nullFreeTimeParam, 0, nullFreeTimeParam.length() - 1));

        if(NumberUtils.isCreatable(timeInString)) {
            int time = Integer.parseInt(timeInString);
            if (timeParam.contains("m")) { // Minutes
                this.time = 60 * time;
            } else if (timeParam.contains("h")) { // Hours
                this.time = 3600 * time;
            } else { // Any others
                this.time = time;
            }
        } else {
            log.warning("Received param is wrong, could not get shutdown delay");
        }
    }

    public void execShutdown() {

        String cmd;

        if(SystemUtils.IS_OS_WINDOWS) {
            cmd = "shutdown -s -t " + this.getTime(); // In Windows, time is in seconds
        } else if(SystemUtils.IS_OS_MAC) {
            cmd = "shutdown -h " + (time == 0 ? "now" : "+" + (time / 60)); // In Mac, time is in minutes
        } else if(SystemUtils.IS_OS_UNIX) {
            cmd = "shutdown " + (time == 0 ? "now" : "+" + (time / 60)); // In Linux, time is in minutes
        } else {
            log.warning("OS is not a Windows, Unix-like or Mac. Sorry.");
            return;
        }

        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            log.severe("Error while shutting down system: " + e);
        }

    }

    public void print() {
        log.info("Shutdown time:" + this.getTime() + " s");
    }
}
