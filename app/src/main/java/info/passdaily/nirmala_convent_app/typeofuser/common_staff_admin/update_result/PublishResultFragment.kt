package info.passdaily.nirmala_convent_app.typeofuser.common_staff_admin.update_result

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatSpinner
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import info.passdaily.nirmala_convent_app.R
import info.passdaily.nirmala_convent_app.databinding.FragmentManageGroupBinding
import info.passdaily.nirmala_convent_app.databinding.FragmentPublishResultBinding
import info.passdaily.nirmala_convent_app.databinding.FragmentSendProgressReportBinding
import info.passdaily.nirmala_convent_app.lib.ProgressBarDialog
import info.passdaily.nirmala_convent_app.model.*
import info.passdaily.nirmala_convent_app.services.Global
import info.passdaily.nirmala_convent_app.services.Status
import info.passdaily.nirmala_convent_app.services.Utils
import info.passdaily.nirmala_convent_app.services.ViewModelFactory
import info.passdaily.nirmala_convent_app.services.client_manager.ApiClient
import info.passdaily.nirmala_convent_app.services.client_manager.NetworkLayerStaff
import info.passdaily.nirmala_convent_app.services.localDB.LocalDBHelper
import info.passdaily.nirmala_convent_app.typeofuser.staff.ToolBarClickListener

@Suppress("DEPRECATION")
class PublishResultFragment : Fragment() {

    var TAG = "StudentToGroup"
    private lateinit var resultViewModel: UpdateResultViewModel
    private var _binding: FragmentPublishResultBinding? = null
    private val binding get() = _binding!!
    var aCCADEMICID = 0
    var cLASSID = 0

    var getYears = ArrayList<GetYearClassExamModel.Year>()
    var getClass = ArrayList<GetYearClassExamModel.Class>()


    private lateinit var localDBHelper : LocalDBHelper
    var adminId = 0
    var schoolId = 0


    var constraintLayoutContent : ConstraintLayout? = null
    var constraintEmpty: ConstraintLayout? = null
    var imageViewEmpty: ImageView? = null
    var textEmpty: TextView? = null
    var shimmerViewContainer: ShimmerFrameLayout? = null

    var recyclerViewVideo : RecyclerView? = null

    var spinnerAcademic : AppCompatSpinner? = null
    var spinnerClass : AppCompatSpinner? = null
    var toolBarClickListener : ToolBarClickListener? = null


    var mContext : Context? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(mContext ==null){
            mContext = context.applicationContext
        }
        try {
            toolBarClickListener = context as ToolBarClickListener
        }catch(e : Exception){
            Log.i(TAG,"Exception $e")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Global.screenState = "staffhomepage"
        toolBarClickListener?.setToolbarName("Publish Result")
        // Inflate the layout for this fragment
        //  return inflater.inflate(R.layout.fragment_objective_exam_list, container, false)
        localDBHelper = LocalDBHelper(requireActivity())
        var user = localDBHelper.viewUser()
        adminId = user[0].ADMIN_ID
        schoolId = user[0].SCHOOL_ID

        resultViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiClient(NetworkLayerStaff.services))
        )[UpdateResultViewModel::class.java]

        // Inflate the layout for this fragment
        _binding = FragmentPublishResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinnerAcademic = binding.spinnerAcademic
        spinnerClass = binding.spinnerClass




        spinnerAcademic?.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long) {
                aCCADEMICID = getYears[position].aCCADEMICID
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        spinnerClass?.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long) {
                cLASSID = getClass[position].cLASSID
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }


        initFunction()


        binding.buttonSubmit.setOnClickListener {
            if(aCCADEMICID != 0 && cLASSID != 0) {
                val builder = AlertDialog.Builder(requireActivity())
                builder.setMessage("Are you sure want to publish result ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->
                        // pb.show();
                        publishResult()
                    }
                    .setNegativeButton(
                        "No"
                    ) { dialog, id -> //  Action for 'NO' Button
                        dialog.cancel()
                    }
                //Creating dialog box
                val alert = builder.create()
                //Setting the title manually
                alert.setTitle("Publish Result")
                alert.show()
                val buttonbackground: Button = alert.getButton(DialogInterface.BUTTON_NEGATIVE)
                buttonbackground.setTextColor(Color.BLACK)
                val buttonbackground1: Button = alert.getButton(DialogInterface.BUTTON_POSITIVE)
                buttonbackground1.setTextColor(Color.BLACK)
            }
        }

    }

    private fun publishResult() {
        resultViewModel.getPublishResult(aCCADEMICID,cLASSID)
            .observe(requireActivity(), Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            val response = resource.data?.body()!!
                            Log.i(TAG,"response $response")
                            progressStop()
                            when {
                                Utils.resultFun(response) == "SUCCESS" -> {
                                    Utils.getSnackBarGreen(requireActivity(), "Result send successfully", constraintLayoutContent!!)
                                }
                               else -> {
                                    Utils.getSnackBar4K(requireActivity(), "Result sending failed", constraintLayoutContent!!)
                                }
                            }
                        }
                        Status.ERROR -> {
                            Log.i(TAG,"ERROR")
                            progressStop()
                            Utils.getSnackBar4K(requireActivity(), "Please try again after sometime", constraintLayoutContent!!)
                        }
                        Status.LOADING -> {
                            progressStart()
                            Log.i(TAG,"loading")
                        }
                    }
                }
            })
    }

    private fun initFunction() {
        resultViewModel.getYearClassExam(adminId, schoolId )
            .observe(requireActivity(), Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            val response = resource.data?.body()!!

                            getYears = response.years as ArrayList<GetYearClassExamModel.Year>
                            var years = Array(getYears.size){""}
                            for (i in getYears.indices) {
                                years[i] = getYears[i].aCCADEMICTIME
                            }
                            if (spinnerAcademic != null) {
                                val adapter = ArrayAdapter(
                                    requireActivity(),
                                    android.R.layout.simple_spinner_dropdown_item,
                                    years
                                )
                                spinnerAcademic?.adapter = adapter
                            }

                            getClass = response.classList as ArrayList<GetYearClassExamModel.Class>
                            var classX = Array(getClass.size){""}
                            for (i in getClass.indices) {
                                classX[i] = getClass[i].cLASSNAME
                            }
                            if (spinnerClass != null) {
                                val adapter = ArrayAdapter(
                                    requireActivity(),
                                    android.R.layout.simple_spinner_dropdown_item,
                                    classX
                                )
                                spinnerClass?.adapter = adapter
                            }


                            Log.i(TAG,"initFunction SUCCESS")

                        }
                        Status.ERROR -> {
                            Log.i(TAG,"initFunction ERROR")
                        }
                        Status.LOADING -> {
                            Log.i(TAG,"initFunction LOADING")
                        }
                    }
                }
            })
    }

    private fun progressStart() {
        val dialog1 = ProgressBarDialog()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down)
        dialog1.isCancelable = false
        dialog1.show(transaction, ProgressBarDialog.TAG)
    }

    fun progressStop() {
        val fragment: ProgressBarDialog? =
            requireActivity().supportFragmentManager.findFragmentByTag(ProgressBarDialog.TAG) as ProgressBarDialog?
        if (fragment != null) {
            requireActivity().supportFragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss()
        }
    }


}
