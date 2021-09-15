import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}
fun Context.toastShort(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
fun<A : Activity> Activity.startNewActivity(activity: Class<A>){
    Intent(this, activity).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)

    }
}
