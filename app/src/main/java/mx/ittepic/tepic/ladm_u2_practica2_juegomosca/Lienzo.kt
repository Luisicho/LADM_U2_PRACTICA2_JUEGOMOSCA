package mx.ittepic.tepic.ladm_u2_practica2_juegomosca

import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import android.widget.Toast

class Lienzo(p:MainActivity) : View(p) {
    val principal = p
    var listaMosca = ArrayList<Mosca>()
    var puntaje = 0
    init {
        (0..99).forEach{
            val mosca = Mosca()
            mosca.imagenMosca = BitmapFactory.decodeResource(this.resources,R.drawable.moscasinfondo2)
            listaMosca.add(mosca)
        }
        crearMoscas(this,listaMosca).start()
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val p = Paint()
        canvas.drawColor(Color.WHITE)
        p.color = Color.BLACK
        p.style = Paint.Style.FILL
        p.textSize = 80f
        canvas.drawText("Moscas Aplastadas ${puntaje}",100f,100f,p)
        (0..99).forEach{
            canvas.drawBitmap(listaMosca[it].imagenMosca,listaMosca[it].x,listaMosca[it].y,p)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action){
            MotionEvent.ACTION_DOWN->{

                var puntero = 0
                while (puntero<100){
                    if(listaMosca[puntero].hitbox(event.getX(),event.getY())){
                        if(listaMosca[puntero].vida == true){
                            puntaje++
                            listaMosca[puntero].vida = false
                        }
                        listaMosca[puntero].imagenMosca = BitmapFactory.decodeResource(this.resources,R.drawable.manchasinfondo2)
                    }
                    puntero++
                }
                invalidate()
            }
        }
        return super.onTouchEvent(event)
    }
}

class crearMoscas(p:Lienzo,m:ArrayList<Mosca>):Thread(){
    val puntero = p
    val todamosca = m
    override fun run(){
        super.run()
        while(true){
            (0..99).forEach {
                todamosca[it].moverMosca()
            }
            puntero.principal.runOnUiThread {
                puntero.invalidate()
            }
            sleep(200)
        }
    }
}