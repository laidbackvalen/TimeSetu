package com.valenpateltimesetu.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.navigation.NavController
import com.valenpateltimesetu.ui.theme.backgroundColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Privacy Policy", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = backgroundColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Last Updated: November 23, 2024",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = "Created by Nullscape",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            SectionTitle("1. Introduction and Scope")
            SectionText(
                "Welcome to TimeSetu, a productivity application designed to help you manage your time effectively using the Pomodoro Technique. This Privacy Policy (\"Policy\") explains how Nullscape (\"we,\" \"our,\" or \"us\") collects, uses, discloses, and safeguards your information when you use our mobile application TimeSetu (the \"Service\")."
            )
            SectionText(
                "By using TimeSetu, you consent to the data practices described in this Policy. If you do not agree with the data practices described in this Policy, you should not use our Service. This Policy applies to all users of TimeSetu, regardless of their location."
            )
            SectionText(
                "We are committed to protecting your privacy and ensuring transparency about our data practices. This Policy is designed to help you understand what information we collect, why we collect it, and how you can manage it."
            )

            SectionTitle("2. Information We Collect")
            SectionText(
                "TimeSetu is designed with privacy as a core principle. We believe in minimal data collection, and our app operates primarily on your device with local storage."
            )
            
            SubSectionTitle("2.1 Information You Provide")
            SectionText(
                "TimeSetu does not require user registration or account creation. However, if you choose to contact us through the feedback mechanism within the app, we may collect the information you voluntarily provide, including but not limited to:"
            )
            SectionText(
                "• Email address (if provided in feedback)\n" +
                "• Feedback content and messages\n" +
                "• Device information you may share in bug reports\n" +
                "• Any other information you choose to provide when contacting us"
            )
            
            SubSectionTitle("2.2 Automatically Collected Information")
            SectionText(
                "TimeSetu is designed to operate with minimal data collection. We do not use analytics tools, tracking technologies, or advertising networks that automatically collect information about your usage patterns."
            )
            SectionText(
                "However, standard technical information may be collected by the app stores (Google Play Store) when you download and update the app, which is governed by their respective privacy policies."
            )
            
            SubSectionTitle("2.3 Local Device Storage")
            SectionText(
                "All timer settings, custom time configurations, preferences, and session data are stored exclusively on your device. This includes:"
            )
            SectionText(
                "• Custom timer durations you configure\n" +
                "• Timer session history (if stored locally)\n" +
                "• App preferences and settings\n" +
                "• Any other data generated through app usage"
            )
            SectionText(
                "This local data never leaves your device unless you explicitly choose to export or share it. We do not have access to this information."
            )

            SectionTitle("3. How We Use Your Information")
            SectionText(
                "Given our minimal data collection approach, any information we do collect is used solely for the following purposes:"
            )
            
            SubSectionTitle("3.1 Service Improvement")
            SectionText(
                "If you provide feedback or contact us, we use that information to:"
            )
            SectionText(
                "• Respond to your inquiries and support requests\n" +
                "• Improve app functionality and user experience\n" +
                "• Fix bugs and resolve technical issues\n" +
                "• Understand user needs and preferences"
            )
            
            SubSectionTitle("3.2 Legal Compliance")
            SectionText(
                "We may use collected information as necessary to comply with legal obligations, respond to legal processes, enforce our Terms of Service, protect our rights, privacy, safety, or property, or protect our users or others."
            )
            
            SubSectionTitle("3.3 Communication")
            SectionText(
                "We may use your contact information to respond to your inquiries, provide customer support, send important updates about the Service, or communicate about changes to this Policy."
            )

            SectionTitle("4. Data Storage and Security")
            SectionText(
                "Your privacy and data security are paramount to us. We implement appropriate technical and organizational measures to protect your information."
            )
            
            SubSectionTitle("4.1 Local Storage")
            SectionText(
                "All app data, including timer configurations, preferences, and session information, is stored locally on your device using Android's standard data storage mechanisms. This data is encrypted at the operating system level and is accessible only to the app and the device user."
            )
            SectionText(
                "When you uninstall TimeSetu, all locally stored data is permanently deleted from your device. We cannot recover this data once deleted."
            )
            
            SubSectionTitle("4.2 Data Transmission")
            SectionText(
                "TimeSetu does not transmit your timer data, preferences, or usage information to our servers or third-party services. All functionality operates entirely on your device."
            )
            SectionText(
                "The only potential data transmission occurs if you:"
            )
            SectionText(
                "• Submit feedback or contact us through the app\n" +
                "• Download app updates from the app store\n" +
                "• Choose to share app data manually (if such feature is added)"
            )
            
            SubSectionTitle("4.3 Security Measures")
            SectionText(
                "While we do not store your data on our servers, we take reasonable measures to ensure the security of any information you may provide when contacting us, including:"
            )
            SectionText(
                "• Secure communication channels for feedback submission\n" +
                "• Regular security assessments of our systems\n" +
                "• Implementation of industry-standard security practices"
            )
            SectionText(
                "However, no method of transmission over the Internet or electronic storage is 100% secure. While we strive to use commercially acceptable means to protect your information, we cannot guarantee absolute security."
            )

            SectionTitle("5. Data Sharing and Disclosure")
            SectionText(
                "We are committed to not selling, renting, or trading your personal information. Our data sharing practices are as follows:"
            )
            
            SubSectionTitle("5.1 No Third-Party Sharing")
            SectionText(
                "We do not share, sell, rent, or trade your personal information with third parties for their commercial purposes. TimeSetu does not integrate with advertising networks, analytics services, or data brokers."
            )
            
            SubSectionTitle("5.2 Service Providers")
            SectionText(
                "We may work with service providers who perform services on our behalf, such as email services for customer support. These service providers are contractually obligated to protect your information and use it only for the purposes we specify."
            )
            
            SubSectionTitle("5.3 Legal Requirements")
            SectionText(
                "We may disclose your information if required to do so by law or in response to valid requests by public authorities (e.g., a court or government agency). We may also disclose information if we believe disclosure is necessary to protect our rights, your safety, or the safety of others."
            )
            
            SubSectionTitle("5.4 Business Transfers")
            SectionText(
                "In the event of a merger, acquisition, reorganization, bankruptcy, or other sale of all or a portion of our assets, your information may be transferred as part of that transaction. We will notify you of any such change in ownership or control of your information."
            )

            SectionTitle("6. Your Rights and Choices")
            SectionText(
                "You have certain rights regarding your personal information, subject to applicable law:"
            )
            
            SubSectionTitle("6.1 Access and Deletion")
            SectionText(
                "Since TimeSetu stores data locally on your device, you have full control over your data. You can:"
            )
            SectionText(
                "• Access all app data directly on your device\n" +
                "• Delete app data by clearing app storage in device settings\n" +
                "• Delete all data by uninstalling the app"
            )
            
            SubSectionTitle("6.2 Data Export")
            SectionText(
                "Currently, TimeSetu does not provide built-in data export functionality. If you wish to preserve your data before uninstalling, you may need to manually note your preferences and settings."
            )
            
            SubSectionTitle("6.3 Opt-Out Rights")
            SectionText(
                "Since we do not collect personal information for marketing purposes, there is no need to opt out of marketing communications. If you have provided contact information and wish to stop receiving communications from us, you can request this by contacting us."
            )
            
            SubSectionTitle("6.4 Rights Under Applicable Laws")
            SectionText(
                "Depending on your location, you may have additional rights under applicable privacy laws, such as:"
            )
            SectionText(
                "• Right to access your personal information\n" +
                "• Right to rectification (correction) of inaccurate data\n" +
                "• Right to erasure (deletion) of your data\n" +
                "• Right to restrict or object to processing\n" +
                "• Right to data portability\n" +
                "• Right to withdraw consent"
            )
            SectionText(
                "To exercise these rights, please contact us using the information provided at the end of this Policy."
            )

            SectionTitle("7. Children's Privacy")
            SectionText(
                "TimeSetu is safe for users of all ages, including children. We do not knowingly collect personal information from children under the age of 13 (or the applicable age of consent in your jurisdiction) without appropriate parental consent."
            )
            SectionText(
                "If we become aware that we have collected personal information from a child without verification of parental consent, we will take steps to delete that information from our records."
            )
            SectionText(
                "If you are a parent or guardian and believe your child has provided us with personal information, please contact us immediately. We encourage parents and guardians to monitor their children's use of mobile applications."
            )

            SectionTitle("8. International Data Transfers")
            SectionText(
                "TimeSetu is operated by Nullscape, which is based in India. If you are using TimeSetu from outside India, please be aware that your information may be transferred to, stored, and processed in India where our servers may be located."
            )
            SectionText(
                "By using TimeSetu, you consent to the transfer of your information to India and processing in accordance with this Policy. We will ensure that appropriate safeguards are in place to protect your information in accordance with this Policy."
            )

            SectionTitle("9. Third-Party Services and Links")
            SectionText(
                "TimeSetu does not integrate third-party analytics, advertising networks, or social media platforms. The app operates independently without external service dependencies for data collection."
            )
            SectionText(
                "However, our website (www.nullscape.in) may contain links to third-party websites or services. We are not responsible for the privacy practices of these external sites. We encourage you to review the privacy policies of any third-party sites you visit."
            )

            SectionTitle("10. Permissions and Device Access")
            SectionText(
                "TimeSetu requests minimal permissions necessary for core functionality:"
            )
            SectionText(
                "• Device storage: To save your timer preferences and settings locally\n" +
                "• Display over other apps (if applicable): For timer notifications during focus sessions"
            )
            SectionText(
                "We do not request unnecessary permissions such as:"
            )
            SectionText(
                "• Location data\n" +
                "• Contacts or phone numbers\n" +
                "• Camera or microphone\n" +
                "• Biometric data\n" +
                "• Calendar access\n" +
                "• Any other sensitive permissions"
            )
            SectionText(
                "You can revoke permissions at any time through your device settings, though this may affect app functionality."
            )

            SectionTitle("11. Cookies and Tracking Technologies")
            SectionText(
                "TimeSetu does not use cookies, web beacons, pixel tags, or similar tracking technologies. The app operates entirely within the mobile application environment without web-based tracking mechanisms."
            )

            SectionTitle("12. Data Retention")
            SectionText(
                "Since TimeSetu stores data locally on your device, data retention is under your control:"
            )
            SectionText(
                "• App data persists on your device until you delete it or uninstall the app\n" +
                "• You can clear app data at any time through device settings\n" +
                "• If you contact us, we retain communication records only as long as necessary to respond to your inquiry and comply with legal obligations"
            )

            SectionTitle("13. Changes to This Privacy Policy")
            SectionText(
                "We may update this Privacy Policy from time to time to reflect changes in our practices, technology, legal requirements, or other factors. We will notify you of any material changes by:"
            )
            SectionText(
                "• Updating the \"Last Updated\" date at the top of this Policy\n" +
                "• Providing notice within the app (if significant changes occur)\n" +
                "• Posting the updated Policy on our website"
            )
            SectionText(
                "We encourage you to review this Policy periodically to stay informed about how we protect your information. Your continued use of TimeSetu after changes become effective constitutes acceptance of the updated Policy."
            )
            SectionText(
                "If you do not agree to the revised Policy, you should discontinue using TimeSetu and uninstall the app."
            )

            SectionTitle("14. Governing Law and Jurisdiction")
            SectionText(
                "This Privacy Policy is governed by and construed in accordance with the laws of India, without regard to its conflict of law principles. Any disputes arising from or relating to this Policy or your use of TimeSetu shall be subject to the exclusive jurisdiction of the courts located in India."
            )

            SectionTitle("15. Contact Information")
            SectionText(
                "If you have any questions, concerns, or requests regarding this Privacy Policy or our data practices, please contact us:"
            )
            
            ContactInfo("Company:", "Nullscape")
            ContactInfo("Website:", "www.nullscape.in")
            ContactInfo("Email:", "we.nullscape@gmail.com")
            
            SectionText(
                "We are committed to addressing your privacy concerns and will respond to your inquiries in a timely manner, typically within 30 days of receiving your request."
            )

            SectionTitle("16. Consent")
            SectionText(
                "By using TimeSetu, you acknowledge that you have read and understood this Privacy Policy and agree to the collection, use, and disclosure of your information as described herein."
            )
            SectionText(
                "If you do not agree with any aspect of this Policy, please do not use TimeSetu. Your use of the Service constitutes your acceptance of this Privacy Policy."
            )

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier.padding(top = 24.dp, bottom = 12.dp)
    )
}

@Composable
private fun SubSectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.White.copy(alpha = 0.9f),
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
    )
}

@Composable
private fun SectionText(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        color = Color.White.copy(alpha = 0.8f),
        lineHeight = 22.sp,
        modifier = Modifier.padding(bottom = 12.dp)
    )
}

@Composable
private fun ContactInfo(label: String, value: String) {
    Row(
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White.copy(alpha = 0.9f),
            modifier = Modifier.width(80.dp)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.8f)
        )
    }
}
