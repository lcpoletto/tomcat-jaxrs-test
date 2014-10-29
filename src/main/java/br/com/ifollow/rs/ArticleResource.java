/**
 * 
 */
package br.com.ifollow.rs;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.ifollow.domain.Article;

/**
 * @author Luis Carlos Poletto
 */
@Path("/article")
@Produces(MediaType.APPLICATION_JSON)
public class ArticleResource implements Serializable {

	private static final long serialVersionUID = 6001379600283113547L;
	
	@GET
	@Path("/get/{page}/{pageSize}")
	public List<Article> get(@PathParam("page") int page, @PathParam("pageSize") int pageSize) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("BlogPU");
		EntityManager em = emf.createEntityManager();
		TypedQuery<Article> query = em.createQuery("select a from Article a", Article.class);
		query.setFirstResult(page * pageSize);
		query.setMaxResults(pageSize);
		return query.getResultList();
	}

}
