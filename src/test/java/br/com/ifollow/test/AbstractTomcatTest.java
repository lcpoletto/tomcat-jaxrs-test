/**
 * 
 */
package br.com.ifollow.test;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.loader.WebappLoader;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.apache.tomcat.util.descriptor.web.ContextResourceLink;
import org.apache.tomcat.util.descriptor.web.NamingResources;
import org.glassfish.jersey.client.ClientConfig;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Luis Carlos Poletto
 *
 */
public class AbstractTomcatTest implements Serializable {
	
	private static final long serialVersionUID = -5139027630186993162L;
	private static final Logger logger = LoggerFactory.getLogger(AbstractTomcatTest.class);
	private static final int PORT = 9998;
	private static final String URL = "http://localhost:" + PORT;

	private static Tomcat tomcat;

	private final AtomicReference<Client> client = new AtomicReference<Client>(null);

	@BeforeClass
	public static void setUpClass() throws LifecycleException, IOException {
		final File baseDir = createBaseDirectory();
		tomcat = new Tomcat();
		tomcat.setBaseDir(baseDir.getAbsolutePath());
		tomcat.setPort(9998);
		tomcat.enableNaming();

		Context context = tomcat.addContext("", baseDir.getAbsolutePath());
		Wrapper wrapper = Tomcat.addServlet(context, "javax.ws.rs.core.Application",
				"org.glassfish.jersey.servlet.ServletContainer");
		wrapper.addInitParameter("jersey.config.server.provider.packages", "br.com.ifollow.rs");
		context.addServletMapping("/*", "javax.ws.rs.core.Application");
		context.setLoader(new WebappLoader(Thread.currentThread().getContextClassLoader()));

		ContextResource blogDS = new ContextResource();
		blogDS.setName("jdbc/BlogDS");
		blogDS.setType("javax.sql.DataSource");
		blogDS.setAuth("Container");
		blogDS.setProperty("url", "jdbc:h2:~/BlogDB");
		blogDS.setProperty("driverClassName", "org.h2.Driver");
		blogDS.setProperty("username", "sa");
		blogDS.setProperty("password", "sa");

		ContextResourceLink blogDSLink = new ContextResourceLink();
		blogDSLink.setGlobal("jdbc/BlogDS");
		blogDSLink.setName("jdbc/BlogDS");
		blogDSLink.setType("javax.sql.DataSource");

		NamingResources globalResources = tomcat.getServer().getGlobalNamingResources();
		NamingResources namingResources = context.getNamingResources();

		globalResources.addResource(blogDS);
		namingResources.addResourceLink(blogDSLink);

		tomcat.start();
	}

	@AfterClass
	public static void tearDownClass() throws LifecycleException {
		tomcat.stop();
	}

	/**
	 * Create a web resource whose URI refers to the base URI the Web application is deployed at.
	 *
	 * @return the created web resource
	 */
	protected WebTarget target() {
		return client().target(URL);
	}

	/**
	 * Create a web resource whose URI refers to the base URI the Web application is deployed at plus the path specified
	 * in the argument.
	 * <p/>
	 * This method is an equivalent of calling {@code target().path(path)}.
	 *
	 * @param path
	 *            Relative path (from base URI) this target should point to.
	 * @return the created web resource
	 */
	protected WebTarget target(String path) {
		return target().path(path);
	}

	/**
	 * Get the client that is configured for this test.
	 *
	 * @return the configured client.
	 */
	protected Client client() {
		return client.get();
	}

	@Before
	public void setUp() throws Exception {
		Client old = client.getAndSet(configureClient());
		close(old);
	}

	@After
	public void tearDown() throws Exception {
		Client old = client.getAndSet(null);
		close(old);
	}

	private static File createBaseDirectory() throws IOException {
		final File result = File.createTempFile("tmpTomcat-", "");

		if (!result.delete()) {
			throw new IOException("Cannot (re)create base folder: " + result.getAbsolutePath());
		}

		if (!result.mkdir()) {
			throw new IOException("Cannot create base folder: " + result.getAbsolutePath());
		}

		return result;
	}

	private Client configureClient() {
		ClientConfig config = new ClientConfig();
		return ClientBuilder.newClient(config);
	}

	private final void close(final Client... clients) {
		if (clients != null && clients.length > 0) {
			for (Client c : clients) {
				if (c != null) {
					try {
						c.close();
					} catch (Throwable t) {
						logger.warn("Error closing a client instance.", t);
					}
				}
			}
		}
	}

}
