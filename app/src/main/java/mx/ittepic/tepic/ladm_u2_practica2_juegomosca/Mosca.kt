package mx.ittepic.tepic.ladm_u2_practica2_juegomosca

import android.graphics.Bitmap
import java.util.*

class Mosca {
    var x = 0f
    var y = 0f
    lateinit var imagenMosca:Bitmap
    var vida = true
    init {
        x = (Math.random()*1000).toFloat()
        y = ((Math.random()*2500)*-1).toFloat()
    }
    fun moverMosca(){
        if (vida){
            y += (Math.random()*70).toFloat()
            if (y>2500) y = ((Math.random()*2500)*-1).toFloat()
        }
    }
    fun hitbox(posx:Float,posy:Float):Boolean{
        if ((posx < x + 100 && posx > x -100) && (posy < y + 100 && posy > y -100)) {
            return true
        }
        return false
    }
}