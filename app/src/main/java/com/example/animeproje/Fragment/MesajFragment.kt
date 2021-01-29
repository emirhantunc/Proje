package com.example.animeproje.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.animeproje.Adapter.MesajAdapter
import com.example.animeproje.R
import com.example.animeproje.model.mesaj
import kotlinx.android.synthetic.main.fragment_mesaj.view.*


/**
 * A simple [Fragment] subclass.
 */
class MesajFragment : Fragment() {
    lateinit var root: View
    var ayanokoji =
        "https://static.wikia.nocookie.net/youkoso-jitsuryoku-shijou-shugi-no-kyoushitsu-e/images/b/bf/Episode_012-07.jpg/revision/latest/scale-to-width-down/340?cb=20170928143111"
    var suzune =
        "data:image/jpeg;base64,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"
    var sakura = "https://i.pinimg.com/564x/4b/d8/fa/4bd8fa273faab35a5652c826545e5742.jpg"

    var mesaj = listOf(
        mesaj("Ayanokoji", "22:47", ayanokoji, "Dün gece eve gelirken yağmur yağdı"),
        mesaj("Suzune", "22:12", suzune, "OK"),
        mesaj("Sakura", "22:09",sakura, "Skida Ayanokoji-Kun"),
        mesaj("Sakura", "22:09",sakura, "Skida Ayanokoji-Kun"),
        mesaj("Sakura", "22:09",sakura, "Skida Ayanokoji-Kun"),
        mesaj("Sakura", "22:09",sakura, "Skida Ayanokoji-Kun"),
        mesaj("Sakura", "22:09",sakura, "Skida Ayanokoji-Kun"),
        mesaj("Sakura", "22:09",sakura, "Skida Ayanokoji-Kun"),
        mesaj("Sakura", "22:09",sakura, "Skida Ayanokoji-Kun"),
        mesaj("Sakura", "22:09",sakura, "Skida Ayanokoji-Kun"),
        mesaj("Sakura", "22:09",sakura, "Skida Ayanokoji-Kun"),
        mesaj("Sakura", "22:09",sakura, "Skida Ayanokoji-Kun"),
        mesaj("Sakura", "22:09",sakura, "Skida Ayanokoji-Kun"),
        mesaj("Sakura", "22:09",sakura, "Skida Ayanokoji-Kun"),
        mesaj("Sakura", "22:09",sakura, "Skida Ayanokoji-Kun"),
        mesaj("Sakura", "22:09",sakura, "Skida Ayanokoji-Kun"),
        mesaj("Sakura", "22:09",sakura, "Skida Ayanokoji-Kun"),
        mesaj("Sakura", "22:09",sakura, "Skida Ayanokoji-Kun"),
        mesaj("Sakura", "22:09",sakura, "Skida Ayanokoji-Kun"),
        mesaj("Sakura", "22:09",sakura, "Skida Ayanokoji-Kun"),
        mesaj("Sakura", "22:09",sakura, "Skida Ayanokoji-Kun"),
        mesaj("Sakura", "22:09",sakura, "Skida Ayanokoji-Kun"),
        mesaj("Sakura", "22:09",sakura, "Skida Ayanokoji-Kun"),
        mesaj("Sakura", "22:09",sakura, "Skida Ayanokoji-Kun"),
        mesaj("Sakura", "22:09",sakura, "Skida Ayanokoji-Kun"),
        mesaj("Sakura", "22:09",sakura, "Skida Ayanokoji-Kun"),
        mesaj("Sakura", "22:09",sakura, "Skida Ayanokoji-Kun"),
        mesaj("Sakura", "22:09",sakura, "Skida Ayanokoji-Kun"),
        mesaj("Sakura", "22:09",sakura, "Skida Ayanokoji-Kun"),
        mesaj("Sakura", "22:09",sakura, "Skida Ayanokoji-Kun")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_mesaj, container, false)
        val adapter = MesajAdapter(mesaj, object : mesajinterface {
            override fun clickitem(a: mesaj, b: Int) {
            }
        })
        root.recymesaj.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        root.recymesaj.adapter = adapter
        return root
    }
}
interface mesajinterface {
    fun clickitem(a: mesaj, b: Int)
}
