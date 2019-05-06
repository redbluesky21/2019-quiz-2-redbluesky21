package id.ac.polinema.todoretrofit.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import id.ac.polinema.todoretrofit.R;
import id.ac.polinema.todoretrofit.generator.ServiceGenerator;
import id.ac.polinema.todoretrofit.models.Envelope;
import id.ac.polinema.todoretrofit.models.Todo;
import id.ac.polinema.todoretrofit.services.TodoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private Context context;
    private List<Todo> items;
    TodoService  service = ServiceGenerator.createService(TodoService.class);
    private OnTodoClickedListener listener;

    public TodoAdapter(Context context, OnTodoClickedListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setItems(List<Todo> items) {
        this.items = items;
        this.notifyDataSetChanged();
    }

    public void setListener(OnTodoClickedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_todo, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Todo todo = items.get(i);
        viewHolder.bind(todo, listener);
    }

    public List<Todo> getItems() {
        return items;
    }

    @Override
    public int getItemCount() {
        return (items != null) ? items.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnCreateContextMenuListener{
        TextView todoText;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            todoText = itemView.findViewById(R.id.text_todo);
            cardView = itemView.findViewById(R.id.card_view);
            cardView.setOnCreateContextMenuListener(this);
        }

        public void bind(final Todo todo, final OnTodoClickedListener listener) {
            todoText.setText(todo.getTodo());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(todo);
                }
            });
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(),121,0,"Delete");
        }
    }

    public interface OnTodoClickedListener {
        void onClick(Todo todo);
    }

    public void deleteItem(final int position) {
        Todo item = items.get(position);
        final View view = ((Activity) context).findViewById(R.id.coordinator_layout);
        items.remove(position);

        Call<Envelope<Todo>> deleteTodo = service.deleteTodo(Integer.toString(item.getId()));
        deleteTodo.enqueue(new Callback<Envelope<Todo>>() {
            @Override
            public void onResponse(Call<Envelope<Todo>> call, Response<Envelope<Todo>> response) {
                if (response.code() == 200) {
                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                    Snackbar snackbar = Snackbar.make(view,"Deleted",
                            Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else{
                    Toast.makeText(context,response.toString(),Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Envelope<Todo>> call, Throwable t) {

            }
        });
    }



}

