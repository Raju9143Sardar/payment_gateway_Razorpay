package com.rs.payment_gateway_razorpay.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import com.rs.payment_gateway_razorpay.R
import com.rs.payment_gateway_razorpay.databinding.ActivityPaymentBinding
import org.json.JSONObject

class PaymentActivity : AppCompatActivity() , PaymentResultWithDataListener {
    private lateinit var binding: ActivityPaymentBinding
    private val PaymentKeyId = "rzp_test_NMYQpu5T4dazyK"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    /**
     * this method is used to initialize the all the required components in activity
     */
    fun init(){
        try{
            paymentSubscription()
        }catch (e:Exception){
            Toast.makeText(this, "Error in payment", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }


    @Throws(Exception::class)
    fun paymentSubscription(){
        Checkout.preload(applicationContext)
        val co = Checkout()
        co.setImage(R.drawable.ic_launcher_foreground)
        co.setKeyID(PaymentKeyId)

        binding.button.setOnClickListener {
            startPayment("20")
        }
    }



    private fun startPayment(paymentAmountRaw: String) {
        /*
        *  You need to pass the current activity to let Razorpay create CheckoutActivity
        * */
        val activity: Activity = this
        val co = Checkout()

        val amount: String = Math.round(paymentAmountRaw.toFloat() * 100).toString()   //calculate amount
        Log.d("TAG", "startPayment: amount= $amount")

        try {
            val options = JSONObject()
            options.put("name",getString(R.string.app_name))    //app name / website name
            options.put("description","Demoing Charges")  // product description
            //You can omit the image option to fetch the image from the Dashboard
            options.put("image","https://media.istockphoto.com/id/1306105191/photo/qr-code-payment-person-paying-with-mobile-phone.jpg?s=2048x2048&w=is&k=20&c=us-eBUeDlUk_DDUZ4mYGV8IywOih_XFIJqnRWQmFXLM=")     //use app Icon but Web link
            options.put("theme.color", getColor(R.color.purple_500));  //app color
            options.put("currency","INR");  //currency type
            //options.put("order_id", "order_DBJOWzybf0sJbb");    //
            options.put("amount",amount)    //set amount in currency subunits

            //how many times user can retry for payment
            val retryObj = JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            //previous define user payment details
            val prefill = JSONObject()
            prefill.put("name","Gaurav Kumar")
            prefill.put("email","gaurav.kumar@example.com")
            prefill.put("contact","9876543210")

            options.put("prefill",prefill)
            co.open(activity,options)
        }catch (e: Exception){
            Toast.makeText(activity,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    //######################################## payment result ################################################
    /**
     * Add logic here for a successful payment response
     */
    override fun onPaymentSuccess(razorpayPaymentId: String?, PaymentData: PaymentData) {
        Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show()
    }

    /**
     * Add logic here for a failed payment response
     */
    override fun onPaymentError(errorCode: Int, response: String?, p2: PaymentData?) {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show()
        Log.d("TAG", "onPaymentError: errorCode= $errorCode \nresponse: $response")
    }



}