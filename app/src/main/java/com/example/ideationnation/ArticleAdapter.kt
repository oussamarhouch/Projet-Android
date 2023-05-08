

package  com.example.ideationnation
import com.example.ideationnation.Idea
import com.example.ideationnation.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ArticleAdapter (private val articles : ArrayList<Idea>): RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {



     class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

           val title :TextView = itemView.findViewById(R.id.title_text_view)

           val content :TextView = itemView.findViewById(R.id.content_text_view)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.article_item, parent, false)
        return ArticleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
       holder.title.text =article.title
        holder.content.text = article.myIdea
    }

    override fun getItemCount(): Int {
        return articles.size
    }


}