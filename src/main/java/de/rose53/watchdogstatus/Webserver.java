package de.rose53.watchdogstatus;


import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

public class Webserver {

    private Server server;

    public void start() throws Exception {

        server = new Server(8090);

        ServletContextHandler contexHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        contexHandler.setContextPath("/resources");
        ServletHolder restEasyServletHolder = new ServletHolder(new HttpServletDispatcher());

        restEasyServletHolder.setInitParameter("javax.ws.rs.Application","de.rose53.watchdogstatus.JaxRsApplication");

        contexHandler.addServlet(restEasyServletHolder, "/*");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { contexHandler });
        server.setHandler(handlers);

        server.start();
    }

    public void stop() throws Exception {
        server.stop();
    }

}