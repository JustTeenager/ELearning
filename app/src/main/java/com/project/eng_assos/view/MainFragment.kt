package com.project.eng_assos.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.*
import com.project.eng_assos.R
import com.project.eng_assos.dagger.component.DaggerMainFragmentComponent
import com.project.eng_assos.dagger.module.BindingModule
import com.project.eng_assos.dagger.module.ContextModule
import com.project.eng_assos.databinding.FragmentMainBinding
import com.project.eng_assos.utils.Callback
import com.project.eng_assos.utils.PaymentsUtil
import com.project.eng_assos.utils.SharedPrefsManager
import java.io.*
import javax.inject.Inject

class MainFragment: Fragment() {

    @Inject
    lateinit var binding:FragmentMainBinding
    @Inject
    lateinit var callback: Callback
    private val priceCents:Long = 1
    private val paymentsClient by lazy { PaymentsUtil.createPaymentsClient(activity!!) }
    private val LOAD_PAYMENT_DATA_REQUEST_CODE = 991

    companion object{
        fun newInstance(): MainFragment {
            val args = Bundle()
            val fragment = MainFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        DaggerMainFragmentComponent.builder().contextModule(activity?.let { ContextModule(it) }).bindingModule(
            BindingModule(inflater, container)
        ).build().inject(this)
        binding.learnButton.setOnClickListener { callback.replaceFragment(LevelsFragment.newInstance()) }
        binding.testButton.setOnClickListener { callback.replaceFragment(LevelsBlockFragment.newInstance()) }
        possiblyShowGooglePayButton()
        binding.googlePayButton
            .setOnClickListener { requestPayment() }
        return binding.root
    }


    private fun possiblyShowGooglePayButton() {

        val isReadyToPayJson = PaymentsUtil.isReadyToPayRequest() ?: return
        val request = IsReadyToPayRequest.fromJson(isReadyToPayJson.toString()) ?: return
        val task = paymentsClient.isReadyToPay(request)
        task.addOnCompleteListener { completedTask ->
            try {
                completedTask.getResult(ApiException::class.java)?.let(::setGooglePayAvailable)
            } catch (exception: ApiException) {
                // Process error
                Log.w("tut", exception)
            }
        }
    }

    /**
     * If isReadyToPay returned `true`, show the button and hide the "checking" text. Otherwise,
     * notify the user that Google Pay is not available. Please adjust to fit in with your current
     * user flow. You are not required to explicitly let the user know if isReadyToPay returns `false`.
     *
     * @param available isReadyToPay API response.
     */
    private fun setGooglePayAvailable(available: Boolean) {
        if (available && context?.let { SharedPrefsManager.read(it,SharedPrefsManager.CODE_TO_PAY) } != SharedPrefsManager.PAYED) {
            binding.googlePayButton.visibility = VISIBLE
        } else if(!available) {
            Toast.makeText(
                context,
                getString(R.string.google_pay_not_available),
                Toast.LENGTH_LONG).show();
        }
    }

    private fun requestPayment() {

        binding.googlePayButton.isClickable = false

        val paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(priceCents)
        if (paymentDataRequestJson == null) {
            Log.e("tut", "Can't fetch payment data request")
            return
        }
        val request = PaymentDataRequest.fromJson(paymentDataRequestJson.toString())
        if (request != null) {
            activity?.let {
                AutoResolveHelper.resolveTask(
                    paymentsClient.loadPaymentData(request), it, LOAD_PAYMENT_DATA_REQUEST_CODE)
            }
        }
    }

    /**
     * Handle a resolved activity from the Google Pay payment sheet.
     *
     * @param requestCode Request code originally supplied to AutoResolveHelper in requestPayment().
     * @param resultCode Result code returned by the Google Pay API.
     * @param data Intent from the Google Pay API containing payment or error data.
     * @see [Getting a result
     * from an Activity](https://developer.android.com/training/basics/intents/result)
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("tut","активити резалт")
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            LOAD_PAYMENT_DATA_REQUEST_CODE -> {
                when (resultCode) {
                    AppCompatActivity.RESULT_OK ->
                        data?.let { intent ->
                            //PaymentData.getFromIntent(intent)?.let(::handlePaymentSuccess)
                            context?.let { SharedPrefsManager.write(it,SharedPrefsManager.CODE_TO_PAY,SharedPrefsManager.PAYED) }
                            Log.d("tut","все хорошо")
                        }

                    AppCompatActivity.RESULT_CANCELED -> {
                        Toast.makeText(context,
                                context?.getString(R.string.cancel_payment),Toast.LENGTH_SHORT)
                                .show()
                        Log.d("tut","отмена")
                    }

                    AutoResolveHelper.RESULT_ERROR -> {
                        //AutoResolveHelper.getStatusFromIntent(data)?.let {
                          //  handleError(it.statusCode)
                        //}
                        Log.d("tut","ощибка")
                    }
                }
                binding.googlePayButton.isClickable = true
            }
        }
    }
}