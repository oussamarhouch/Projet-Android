

package  com.example.ideationnation
import android.content.Intent
import com.example.ideationnation.Idea
import com.example.ideationnation.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
class IdeasAdapter(private val articles: ArrayList<Idea>) : RecyclerView.Adapter<IdeasAdapter.ArticleViewHolder>() {

    class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        val buttonComment = itemView.findViewById<Button>(R.id.commBtn)
        val title: TextView = itemView.findViewById(R.id.title_text_view)
        val content: TextView = itemView.findViewById(R.id.content_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.allideas, parent, false)
        return ArticleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
        holder.title.text = article.title
        holder.content.text = article.myIdea
        holder.buttonComment.setOnClickListener {
            val intent = Intent(holder.itemView.context, CommentActivity::class.java)

            holder.itemView.context.startActivity(intent)
        }
        holder.ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            Toast.makeText(holder.itemView.context, "Rated: $rating!", Toast.LENGTH_SHORT).show()
            // Vous pouvez ajouter ici le code pour enregistrer la note dans votre base de données ou effectuer d'autres actions.
        }

    }

    override fun getItemCount(): Int {
        return articles.size
    }
}
