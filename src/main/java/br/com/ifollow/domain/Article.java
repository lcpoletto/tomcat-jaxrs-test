/**
 * 
 */
package br.com.ifollow.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Luis Carlos Poletto
 *
 */
@Entity
@Table(name = "ARTICLE")
public class Article implements Serializable {

	private static final long serialVersionUID = 8207090641837930692L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ARTICLE_ID", nullable = false)
	private Long id;

	@Column(name = "TEXT", nullable = false)
	private String text;

	/**
	 * Getter for id.
	 * 
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Setter for id.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Getter for text.
	 * 
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Setter for text.
	 * 
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

}
