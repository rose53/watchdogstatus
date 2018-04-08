package de.rose53.watchdogstatus;

public class WatchdogStatus implements Runnable {

    private boolean running;

    Webserver webServer;

    public WatchdogStatus() {
    	webServer = new Webserver();
    }

    public void start() {

        try {
            System.out.print("Starting WebServer ...");
            webServer.start();
            System.out.println("\b\b\bdone.");
        } catch (Exception e) {
        	System.err.println(e);
        }

        System.out.println("\n\n ####################################################### ");
        System.out.println(" ####           WatchdogStatus IS ALIVE !!!          ### ");
        System.out.println(" ####################################################### ");
        running = true;
    }

    public void stop() {
        if (running) {
            running = false;
        }
        try {
            webServer.stop();
            WS2812B.instance().close();
        } catch (Exception e) {
        	System.err.println(e);
        }
    }

    @Override
    public void run() {
        start();

        while (running) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            	System.out.println(e);
            }
        }
    }


    public static void main(String[] args) {
        WatchdogStatus watchdogStatus = new WatchdogStatus();

        System.out.println("Starting WatchdogStatus ...");
        Thread thread = new Thread(watchdogStatus);
        thread.start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                watchdogStatus.stop();
            }
        });

    }

}
