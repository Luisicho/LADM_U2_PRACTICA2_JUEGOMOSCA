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
    var puntaje = 0
    var pai = Paint()
    var conta = contador(this)
    var todamosca = crearMoscas(this,ArrayList<Mosca>())
    init {

        todamosca.start()
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if(conta.inicio() == false){
            conta.setCanvas(canvas)
            conta.start()
        }
        canvas.drawColor(Color.WHITE)
        pai.color = Color.BLACK
        pai.style = Paint.Style.FILL
        pai.textSize = 80f
        canvas.drawText("Moscas Aplastadas ${puntaje}",100f,100f,pai)
        canvas.drawText(conta.segundos,150f,200f,pai)
        (0..99).forEach{
            canvas.drawBitmap(todamosca.listaMosca[it].imagenMosca,todamosca.listaMosca[it].x,todamosca.listaMosca[it].y,pai)
        }
        if (conta.final){
            pai.color = Color.RED
            canvas.drawText("Se acabo el tempo",250f,1000f,pai)
            todamosca.parar()
            return
        }
        if(puntaje == 100 && conta.final == false){
            pai.color = Color.YELLOW
            conta.parar()
            canvas.drawText("GANASTE ",250f,1000f,pai)
            todamosca.parar()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action){
            MotionEvent.ACTION_DOWN->{

                var puntero = 0
                while (puntero < 100){
                    if(todamosca.listaMosca[puntero].hitbox(event.getX(),event.getY())){
                        if(todamosca.listaMosca[puntero].vida == true){
                            puntaje++
                            todamosca.listaMosca[puntero].vida = false
                        }
                        todamosca.listaMosca[puntero].imagenMosca = BitmapFactory.decodeResource(this.resources,R.drawable.manchasinfondo2)
                    }
                    puntero++
                }
                invalidate()
            }
        }
        return super.onTouchEvent(event)
    }
}
class contador(l:Lienzo):Thread(){
    var lien = l
    var can:Canvas ?= null
    var i = 60
    var inicio = false
    var segundos = "60 Segundos"
    var continua = true
    var final = false
    override fun run() {
        inicio = true
        super.run()
        while (continua){
            if (can != null){
                i--
                segundos = "${i} Segundos"
                if (i == 0){
                    final = true
                    lien.invalidate()
                    return
                }
                lien.invalidate()
                sleep(1000)
            }
        }
    }
    fun parar(){
        continua = false
    }
    fun setCanvas(c:Canvas){
        can = c
    }
    fun getTiempo():Int{
        return i
    }
    fun inicio():Boolean{
        return inicio
    }
}
class crearMoscas(p:Lienzo,m:ArrayList<Mosca>):Thread(){
    val puntero = p
    val listaMosca = m
    var continua = true
    init {
        (0..99).forEach{
            val mosca = Mosca()
            mosca.imagenMosca = BitmapFactory.decodeResource(puntero.resources,R.drawable.moscasinfondo2)
            listaMosca.add(mosca)
        }
    }
    override fun run(){
        super.run()
        while(continua){
            (0..99).forEach {
                listaMosca[it].moverMosca()
            }
            puntero.principal.runOnUiThread {
                puntero.invalidate()
            }
            sleep(200)
        }
    }
    fun parar(){
        continua = false
    }
}