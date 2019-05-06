package id.ac.polinema.todoretrofit.services;

import java.util.List;

import id.ac.polinema.todoretrofit.models.Envelope;
import id.ac.polinema.todoretrofit.models.Todo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TodoService {

	@GET("/v1/todos")
	Call<Envelope<List<Todo>>> getTodos(@Query("q") String q, @Query("page") int page, @Query("pageSize") int pageSize);

	@POST("/v1/todos")
	Call<Envelope<Todo>> addTodo(@Body Todo todo);

	@PUT("/v1/todos/{id}")
	Call<Envelope<Todo>> updateTodo(@Path("id") String id , @Body Todo todo);

	@DELETE("/v1/todos/{id}")
	Call<Envelope<Todo>> deleteTodo(@Path("id") String id);
}
