/**
 * 
 */
package br.com.ifollow.test.fixtures;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ifollow.domain.Article;
import br.com.ifollow.test.AbstractTomcatTest;

/**
 * @author Luis Carlos Poletto
 *
 */
public class ArticleResourceTest extends AbstractTomcatTest {

	private static final long serialVersionUID = 1497897440385430063L;
	private static final Logger logger = LoggerFactory.getLogger(ArticleResourceTest.class);

	@Test
	public void test_get() {
		logger.info("Testing get.");
		List<Article> result = target("/article").path("get").path("0").path("10").request(APPLICATION_JSON).get(List.class);
		Assert.assertNotNull(result);
	}

}
