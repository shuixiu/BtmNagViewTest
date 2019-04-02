package com.test.sixpro.ui.attion

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.widget.TextView
import com.test.sixpro.R
import com.test.sixpro.base.BaseActivity

 class KotlineDemo : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)

       val tv = findView<TextView>(R.id.tv_kotlin)
        tv.setOnClickListener{view->
            Snackbar.make(view,"kotlin_show",Snackbar.LENGTH_LONG).setAction("Action",null).show()
        }
    }

     override fun onResume() {
         super.onResume()
     }

     override fun onStop() {
         super.onStop()
     }


}
