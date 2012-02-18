package com.gtug.shaircard.servlet;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Entity;
import com.google.gson.Gson;
import com.gtug.shaircard.model.EMFService;
import com.gtug.shaircard.model.Event;
import com.gtug.shaircard.model.VCard;

public class AddCard extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/json");
		String body = Util.getPostBody(req);
		Gson gson = new Gson();
		VCard e = gson.fromJson(body, VCard.class);
		
		EntityManager em = EMFService.get().createEntityManager();
		em.persist(e);
		em.close();
		
		em = EMFService.get().createEntityManager();
		em.getTransaction().begin();
		Event event = em.find(Event.class, e.getEventId());
		event.incPeopleCount();
		em.getTransaction().commit();
		em.close();
		
		resp.getWriter().println("SUCCESS");
	}

}
