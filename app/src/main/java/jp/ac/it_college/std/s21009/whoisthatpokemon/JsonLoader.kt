package jp.ac.it_college.std.s21009.whoisthatpokemon

import android.content.res.AssetManager
import com.google.gson.Gson
import java.io.InputStreamReader
import kotlin.reflect.KClass

fun <T : Any> parseJson(assets: AssetManager, file: String, c: KClass<T>): T {
    val inputStream = assets.open(file)
    val jsonReader = InputStreamReader(inputStream, "UTF-8").readText()
    inputStream.close()
    return Gson().fromJson(jsonReader, c.java)
}