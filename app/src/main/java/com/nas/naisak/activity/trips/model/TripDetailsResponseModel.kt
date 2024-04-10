import com.google.gson.annotations.SerializedName

data class TripDetailsResponseModel(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("validation_errors")
    val validationErrors: ArrayList<Any>,
    @SerializedName("data")
    val data: Data
) {
    data class Data(
        @SerializedName("lists")
        val lists: Lists,
        @SerializedName("choices_exceed")
        val choices_exceed: String,
        @SerializedName("no_of_trips_exceed")
        val no_of_trips_exceed: String
    )

    data class Lists(
        @SerializedName("id")
        val id: Int,
        @SerializedName("trip_name_en")
        val tripNameEn: String,
        @SerializedName("trip_name_ar")
        val tripNameAr: String,
        @SerializedName("trip_start_date")
        val tripStartDate: String,
        @SerializedName("trip_end_date")
        val tripEndDate: String,
        @SerializedName("registration_start_date")
        val registrationStartDate: String,
        @SerializedName("registration_end_date")
        val registrationEndDate: String,
        @SerializedName("total_price")
        val totalPrice: String,
        @SerializedName("trip_image")
        val tripImage: ArrayList<String>,
        @SerializedName("coordinator_name")
        val coordinatorName: String,
        @SerializedName("coordinator_email")
        val coordinatorEmail: String,
        @SerializedName("coordinator_phone")
        val coordinatorPhone: String,
        @SerializedName("coordinator_wp")
        val coordinatorWp: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("trip_status")
        val tripStatus: Int,
        @SerializedName("documents_required")
        val documentsRequired: DocumentsRequired,
        @SerializedName("installment_details")
        val installmentDetails: ArrayList<InstallmentDetail>,
        @SerializedName("document_upload_status")
        val documentUploadStatus: DocumentUploadStatus,
        @SerializedName("invoices")
        val invoices: ArrayList<Invoice>
    )

    data class DocumentsRequired(
        @SerializedName("passport_doc")
        val passportDoc: Int,
        @SerializedName("visa_doc")
        val visaDoc: Int,
        @SerializedName("emirates_doc")
        val emiratesDoc: Int,
        @SerializedName("consent_doc")
        val consentDoc: Int
    )

    data class InstallmentDetail(
        @SerializedName("id")
        val id: Int,
        @SerializedName("amount")
        val amount: String,
        @SerializedName("due_date")
        val dueDate: String,
        @SerializedName("paid_status")
        val paidStatus: Int,
        @SerializedName("firstname")
        val firstname: String,
        @SerializedName("email")
        val email: String,
        @SerializedName("paid_amount")
        val paidAmount: String,
        @SerializedName("balance_amount")
        val balanceAmount: Int,
        @SerializedName("invoice_no")
        val invoiceNo: String,
        @SerializedName("payment_date")
        val paymentDate: String,
        @SerializedName("invoice_note")
        val invoiceNote: String,
        @SerializedName("trn_no")
        val trnNo: String,
        @SerializedName("payment_type")
        val paymentType: String
    )

    data class DocumentUploadStatus(
        @SerializedName("passport_status")
        val passportStatus: Int,
        @SerializedName("visa_status")
        val visaStatus: Int,
        @SerializedName("emirates_status")
        val emiratesStatus: Int,
        @SerializedName("consent_status")
        val consentStatus: Int,
        @SerializedName("document_completion_status")
        val documentCompletionStatus: Int
    )

    data class Invoice(
        @SerializedName("id")
        val id: Int,
        @SerializedName("firstname")
        val firstname: String,
        @SerializedName("email")
        val email: String,
        @SerializedName("paid_amount")
        val paidAmount: String,
        @SerializedName("invoice_no")
        val invoiceNo: String,
        @SerializedName("payment_date")
        val paymentDate: String,
        @SerializedName("invoice_note")
        val invoiceNote: String,
        @SerializedName("trn_no")
        val trnNo: String,
        @SerializedName("payment_type")
        val paymentType: String
    )
}


