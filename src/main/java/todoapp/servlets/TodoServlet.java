package todoapp.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import todoapp.model.Todo;

@WebServlet("/todo/*")
public class TodoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public TodoServlet() {}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa");
		EntityManager em = emf.createEntityManager();
		
		String jpql = "select t from Todo t";
		TypedQuery<Todo> query = em.createQuery(jpql, Todo.class);
		List<Todo> todos = query.getResultList();
		response.setStatus(200);
		PrintWriter out = response.getWriter();
		out.println(new Gson().toJson(todos));
		out.flush();
		em.close();
		emf.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa");
		EntityManager em = emf.createEntityManager();
		response.setStatus(203);
		PrintWriter out = response.getWriter();
		StringBuilder json = new StringBuilder();
		BufferedReader reader = request.getReader();
		String linha;
		
		while((linha = reader.readLine()) != null) {
			json.append(linha);
		}
		
		Todo todo = new Gson().fromJson(json.toString(), Todo.class);
		em.getTransaction().begin();
		em.persist(todo);
		em.getTransaction().commit();
		
		out.flush();
		em.close();
		emf.close();
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getPathInfo().replace("/", "");
		if(id.equals("")) {
			response.setStatus(404);
			return;
		}
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa");
		EntityManager em = emf.createEntityManager();
		response.setStatus(203);
		PrintWriter out = response.getWriter();
		StringBuilder json = new StringBuilder();
		BufferedReader reader = request.getReader();
		String linha;
		
		while((linha = reader.readLine()) != null) {
			json.append(linha);
		}
		
		Todo todo = new Gson().fromJson(json.toString(), Todo.class);
		em.getTransaction().begin();
		Todo hasTodo = em.find(Todo.class, Integer.valueOf(id));
		if(hasTodo == null) {
			response.setStatus(404);
			em.close();
			emf.close();
			return;
		}
		hasTodo.setName(todo.getName());
		em.merge(hasTodo);
		em.getTransaction().commit();
		
		out.flush();
		em.close();
		emf.close();
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getPathInfo().replace("/", "");
		if(id.equals("")) {
			response.setStatus(404);
			return;
		}
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa");
		EntityManager em = emf.createEntityManager();
		response.setStatus(201);

		em.getTransaction().begin();
		Todo hasTodo = em.find(Todo.class, Integer.valueOf(id));
		if(hasTodo == null) {
			response.setStatus(404);
			em.close();
			emf.close();
			return;
		}
		em.remove(hasTodo);
		em.getTransaction().commit();
		
		em.close();
		emf.close();
	}
}
