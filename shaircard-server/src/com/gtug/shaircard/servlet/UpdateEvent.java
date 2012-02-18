package com.gtug.shaircard.servlet;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.gtug.shaircard.model.EMFService;
import com.gtug.shaircard.model.Event;

public class UpdateEvent extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/json");
		String body = Util.getPostBody(req);
		Gson gson = new Gson();
		Event e = gson.fromJson(body, Event.class);
		
		EntityManager em = EMFService.get().createEntityManager();
		em.getTransaction().begin();
		Query q = em.createQuery("SELECT e FROM Event e WHERE e.id = :id");
		q.setParameter("id", e.getId());
		Event dbEvent = (Event)q.getSingleResult();
		
		if (e.getCreatorId().equals(dbEvent.getCreatorId())) {
			dbEvent.setAddress(e.getAddress());
			dbEvent.setDescription(e.getDescription());
			dbEvent.setLatitude(e.getLatitude());
			dbEvent.setLongitude(e.getLongitude());
			dbEvent.setName(e.getName());
			dbEvent.setTimeBegin(e.getTimeBegin());
			dbEvent.setTimeEnd(e.getTimeEnd());
			em.getTransaction().commit();
			resp.getWriter().println("SUCCESS");
		} else {
			em.getTransaction().commit();
			resp.getWriter().println("FAILURE");
		}
	}

}