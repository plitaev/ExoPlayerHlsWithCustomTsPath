package plita.me.exoplayerhlswithcustomtspath

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var QUALITY: String="high"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextToken = findViewById<EditText>(R.id.editTextToken)
        val editTextFilename = findViewById<EditText>(R.id.editTextFilename)
        val buttonAPI = findViewById<Button>(R.id.buttonAPI)
        val buttonPlayer = findViewById<Button>(R.id.buttonPlayer)
        val spinner: Spinner = findViewById(R.id.spinner_quality)
        spinner.onItemSelectedListener = this


        ArrayAdapter.createFromResource(
            this,
            R.array.quality_array,
            android.R.layout.simple_spinner_item
        ).also { adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }


        buttonPlayer.isEnabled=false

        buttonAPI!!.setOnClickListener {
            buttonAPI.text=getString(R.string.hint_button_api_process)
            Fuel.get(getString(R.string.server_address) + "api/live/0?quality="+QUALITY).appendHeader(
                getString(
                    R.string.server_login
                ) to getString(R.string.server_password)
            ).responseJson { request1, response1, result1 ->
                editTextToken.setText(result1.get().obj().getString("Token"))
                editTextFilename.setText(result1.get().obj().getString("Filename"))
                buttonPlayer.isEnabled=true
                buttonAPI.text=getString(R.string.hint_button_api)
            }
        }

        buttonPlayer!!.setOnClickListener {
            val token: String=editTextToken.text.toString()
            val filename: String=editTextFilename.text.toString()
            if (token.isNotEmpty() && filename.isNotEmpty()) {
                val intent = Intent("plita.me.exoplayerhlswithcustomtspath.PlayerActivity")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("token", token)
                intent.putExtra("filename", filename)
                startActivity(intent)
            } else {
                Toast.makeText(
                    applicationContext,
                    "Токен и имя файла должны быть указаны!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val choose = resources.getStringArray(R.array.quality_array)
        QUALITY=choose[position]
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}