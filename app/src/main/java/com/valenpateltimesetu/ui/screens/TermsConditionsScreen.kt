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
fun TermsConditionsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Terms & Conditions", color = Color.White) },
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

            SectionTitle("1. Acceptance of Terms")
            SectionText(
                "Welcome to TimeSetu. These Terms and Conditions (\"Terms\") constitute a legally binding agreement between you (\"User,\" \"you,\" or \"your\") and Nullscape (\"Company,\" \"we,\" \"us,\" or \"our\") governing your access to and use of the TimeSetu mobile application (the \"App\" or \"Service\")."
            )
            SectionText(
                "By downloading, installing, accessing, or using TimeSetu, you acknowledge that you have read, understood, and agree to be bound by these Terms and our Privacy Policy, which is incorporated herein by reference. If you do not agree with any part of these Terms, you must not access or use the Service."
            )
            SectionText(
                "These Terms apply to all users of TimeSetu, including without limitation users who are browsers, vendors, customers, merchants, and/or contributors of content. Your access to and use of the Service is conditioned on your acceptance of and compliance with these Terms."
            )

            SectionTitle("2. Description of Service")
            SectionText(
                "TimeSetu is a productivity application designed to help users manage their time effectively using the Pomodoro Technique. The App provides:"
            )
            SectionText(
                "• Timer functionality for focused work sessions\n" +
                "• Customizable time intervals for work and break periods\n" +
                "• Visual progress indicators for timer sessions\n" +
                "• Local storage of user preferences and settings\n" +
                "• Additional features as may be added from time to time"
            )
            SectionText(
                "TimeSetu is provided for personal, non-commercial use. The Service is available for download and use on compatible Android devices through the Google Play Store or other authorized distribution channels."
            )

            SectionTitle("3. Eligibility and Account Requirements")
            SectionText(
                "TimeSetu does not require user registration or account creation. However, to use the Service, you must:"
            )
            SectionText(
                "• Be at least 13 years of age (or the age of majority in your jurisdiction)\n" +
                "• Have the legal capacity to enter into binding agreements\n" +
                "• Comply with all applicable laws and regulations\n" +
                "• Not be prohibited from using the Service under applicable law"
            )
            SectionText(
                "If you are using TimeSetu on behalf of an organization, you represent and warrant that you have the authority to bind that organization to these Terms, and the terms \"you\" and \"your\" will include both you and the organization."
            )

            SectionTitle("4. License to Use")
            SubSectionTitle("4.1 Grant of License")
            SectionText(
                "Subject to your compliance with these Terms, Nullscape grants you a limited, non-exclusive, non-transferable, non-sublicensable, revocable license to download, install, and use TimeSetu on compatible devices that you own or control, solely for your personal, non-commercial purposes."
            )
            
            SubSectionTitle("4.2 License Restrictions")
            SectionText(
                "You may not:"
            )
            SectionText(
                "• Copy, modify, adapt, alter, translate, or create derivative works of the App\n" +
                "• Reverse engineer, disassemble, decompile, or otherwise attempt to derive the source code of the App\n" +
                "• Remove, alter, or obscure any proprietary notices, labels, or marks on the App\n" +
                "• Rent, lease, loan, sell, sublicense, assign, or otherwise transfer the App or your rights thereto\n" +
                "• Use the App for any illegal purpose or in violation of any applicable laws\n" +
                "• Use the App to harm, threaten, or harass others\n" +
                "• Interfere with or disrupt the Service or servers or networks connected to the Service\n" +
                "• Attempt to gain unauthorized access to the Service or related systems"
            )

            SectionTitle("5. Intellectual Property Rights")
            SubSectionTitle("5.1 Ownership")
            SectionText(
                "TimeSetu and all content, features, functionality, and materials available through the Service, including but not limited to text, graphics, logos, icons, images, audio clips, digital downloads, data compilations, and software (collectively, the \"Content\"), are owned by Nullscape, its licensors, or other providers of such material and are protected by copyright, trademark, patent, trade secret, and other intellectual property or proprietary rights laws."
            )
            
            SubSectionTitle("5.2 Trademarks")
            SectionText(
                "The TimeSetu name, logo, and all related names, logos, product and service names, designs, and slogans are trademarks of Nullscape or its affiliates or licensors. You may not use such marks without the prior written permission of Nullscape."
            )
            
            SubSectionTitle("5.3 User Content")
            SectionText(
                "Since TimeSetu operates with local storage and does not transmit user-generated content to our servers, any data you create within the App remains your property. However, by using the Service, you grant Nullscape a non-exclusive, worldwide, royalty-free license to use, reproduce, and display such content solely for the purpose of providing and improving the Service, subject to our Privacy Policy."
            )

            SectionTitle("6. User Responsibilities and Acceptable Use")
            SubSectionTitle("6.1 Acceptable Use")
            SectionText(
                "You agree to use TimeSetu only for lawful purposes and in accordance with these Terms. You agree not to use the Service:"
            )
            SectionText(
                "• In any way that violates any applicable federal, state, local, or international law or regulation\n" +
                "• To transmit, or procure the sending of, any advertising or promotional material without our prior written consent\n" +
                "• To impersonate or attempt to impersonate the Company, a Company employee, another user, or any other person or entity\n" +
                "• To engage in any other conduct that restricts or inhibits anyone's use or enjoyment of the Service, or which may harm the Company or users of the Service"
            )
            
            SubSectionTitle("6.2 User Conduct")
            SectionText(
                "You are solely responsible for your conduct while using TimeSetu. You agree to:"
            )
            SectionText(
                "• Use the Service in compliance with all applicable laws and regulations\n" +
                "• Respect the intellectual property rights of others\n" +
                "• Not use the Service in any manner that could damage, disable, overburden, or impair the Service\n" +
                "• Not attempt to interfere with the proper working of the Service"
            )

            SectionTitle("7. Privacy and Data Protection")
            SectionText(
                "Your use of TimeSetu is also governed by our Privacy Policy, which can be accessed within the App's Settings menu. Please review the Privacy Policy to understand our practices regarding the collection, use, and disclosure of your information."
            )
            SectionText(
                "By using TimeSetu, you consent to the collection and use of information in accordance with our Privacy Policy. We are committed to protecting your privacy and ensuring the security of your data."
            )

            SectionTitle("8. Modifications to Service")
            SectionText(
                "Nullscape reserves the right, in its sole discretion, to modify, suspend, or discontinue, temporarily or permanently, the Service or any part thereof at any time, with or without notice. You agree that Nullscape shall not be liable to you or to any third party for any modification, suspension, or discontinuance of the Service."
            )
            SectionText(
                "We may release updates to the App from time to time. These updates may include bug fixes, feature improvements, or new functionality. You may need to install updates to continue using the Service. Updates may be automatically installed if enabled in your device settings."
            )

            SectionTitle("9. Third-Party Services and Content")
            SectionText(
                "TimeSetu may provide links to third-party websites or services. These links are provided for your convenience only. Nullscape does not endorse, control, or assume responsibility for any third-party websites, services, or content."
            )
            SectionText(
                "Your interactions with third-party services are solely between you and the third party. Nullscape is not responsible for any loss or damage that may result from your use of third-party services or content."
            )

            SectionTitle("10. Disclaimers and Limitations of Liability")
            SubSectionTitle("10.1 Service Provided \"As Is\"")
            SectionText(
                "TIMESETU IS PROVIDED ON AN \"AS IS\" AND \"AS AVAILABLE\" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, NON-INFRINGEMENT, OR COURSE OF PERFORMANCE."
            )
            SectionText(
                "Nullscape does not warrant that:"
            )
            SectionText(
                "• The Service will be available at all times or uninterrupted\n" +
                "• The Service will be error-free or free from viruses or other harmful components\n" +
                "• Any defects or errors will be corrected\n" +
                "• The Service will meet your specific requirements or expectations"
            )
            
            SubSectionTitle("10.2 Limitation of Liability")
            SectionText(
                "TO THE FULLEST EXTENT PERMITTED BY APPLICABLE LAW, NULLSCAPE, ITS AFFILIATES, LICENSORS, SERVICE PROVIDERS, EMPLOYEES, AGENTS, OFFICERS, OR DIRECTORS SHALL NOT BE LIABLE FOR ANY INDIRECT, INCIDENTAL, SPECIAL, CONSEQUENTIAL, OR PUNITIVE DAMAGES, INCLUDING BUT NOT LIMITED TO LOSS OF PROFITS, DATA, USE, GOODWILL, OR OTHER INTANGIBLE LOSSES, RESULTING FROM:"
            )
            SectionText(
                "• Your use or inability to use the Service\n" +
                "• Any unauthorized access to or use of our servers or any personal information stored therein\n" +
                "• Any interruption or cessation of transmission to or from the Service\n" +
                "• Any bugs, viruses, trojan horses, or the like that may be transmitted through the Service"
            )
            SectionText(
                "IN NO EVENT SHALL NULLSCAPE'S TOTAL LIABILITY TO YOU FOR ALL DAMAGES EXCEED THE AMOUNT YOU PAID TO NULLSCAPE, IF ANY, FOR ACCESSING OR USING THE SERVICE, OR $50 (USD), WHICHEVER IS GREATER."
            )
            SectionText(
                "Some jurisdictions do not allow the exclusion or limitation of certain warranties or the limitation of liability for incidental or consequential damages, so some of the above limitations may not apply to you."
            )

            SectionTitle("11. Indemnification")
            SectionText(
                "You agree to defend, indemnify, and hold harmless Nullscape, its affiliates, licensors, and service providers, and its and their respective officers, directors, employees, contractors, agents, licensors, suppliers, successors, and assigns from and against any claims, liabilities, damages, judgments, awards, losses, costs, expenses, or fees (including reasonable attorneys' fees) arising out of or relating to your violation of these Terms or your use of the Service, including, but not limited to, your User Content, any use of the Service's content, services, and products other than as expressly authorized in these Terms, or your use of any information obtained from the Service."
            )

            SectionTitle("12. Termination")
            SubSectionTitle("12.1 Termination by You")
            SectionText(
                "You may stop using TimeSetu at any time by uninstalling the App from your device. Uninstalling the App will delete all locally stored data. You may also delete the App's data through your device settings without uninstalling."
            )
            
            SubSectionTitle("12.2 Termination by Us")
            SectionText(
                "We may terminate or suspend your access to the Service immediately, without prior notice or liability, for any reason whatsoever, including without limitation if you breach these Terms."
            )
            SectionText(
                "Upon termination, your right to use the Service will immediately cease. If you wish to terminate these Terms, you may simply discontinue using the Service and uninstall the App."
            )
            
            SubSectionTitle("12.3 Effect of Termination")
            SectionText(
                "All provisions of these Terms which by their nature should survive termination shall survive termination, including, without limitation, ownership provisions, warranty disclaimers, indemnity, and limitations of liability."
            )

            SectionTitle("13. Governing Law and Dispute Resolution")
            SubSectionTitle("13.1 Governing Law")
            SectionText(
                "These Terms shall be governed by and construed in accordance with the laws of India, without regard to its conflict of law provisions. You agree to submit to the personal and exclusive jurisdiction of the courts located in India for any disputes arising out of or relating to these Terms or the Service."
            )
            
            SubSectionTitle("13.2 Dispute Resolution")
            SectionText(
                "Any dispute, controversy, or claim arising out of or relating to these Terms or the Service shall be resolved through good faith negotiations. If the parties cannot resolve the dispute through negotiation within 30 days, the dispute shall be submitted to binding arbitration in accordance with the rules of the Indian Arbitration and Conciliation Act, 2015."
            )
            SectionText(
                "Notwithstanding the foregoing, Nullscape may seek injunctive or other equitable relief to protect its intellectual property rights in any court of competent jurisdiction."
            )

            SectionTitle("14. Changes to Terms")
            SectionText(
                "Nullscape reserves the right, at its sole discretion, to modify or replace these Terms at any time. If a revision is material, we will provide at least 30 days' notice prior to any new terms taking effect. What constitutes a material change will be determined at our sole discretion."
            )
            SectionText(
                "By continuing to access or use the Service after any revisions become effective, you agree to be bound by the revised terms. If you do not agree to the new terms, you are no longer authorized to use the Service and must uninstall the App."
            )
            SectionText(
                "You are advised to review these Terms periodically for any changes. The date of the last update is indicated at the top of this document."
            )

            SectionTitle("15. Severability")
            SectionText(
                "If any provision of these Terms is found to be invalid, illegal, or unenforceable by a court of competent jurisdiction, the validity, legality, and enforceability of the remaining provisions shall remain in full force and effect. The invalid, illegal, or unenforceable provision shall be modified to the minimum extent necessary to make it valid, legal, and enforceable."
            )

            SectionTitle("16. Entire Agreement")
            SectionText(
                "These Terms, together with our Privacy Policy and any other legal notices published by Nullscape on the Service, shall constitute the entire agreement between you and Nullscape concerning the Service. These Terms supersede all prior or contemporaneous communications and proposals, whether electronic, oral, or written, between you and Nullscape with respect to the Service."
            )

            SectionTitle("17. Waiver")
            SectionText(
                "No waiver by Nullscape of any term or condition set forth in these Terms shall be deemed a further or continuing waiver of such term or condition or a waiver of any other term or condition, and any failure of Nullscape to assert a right or provision under these Terms shall not constitute a waiver of such right or provision."
            )

            SectionTitle("18. Assignment")
            SectionText(
                "You may not assign or transfer these Terms or any of your rights or obligations hereunder without Nullscape's prior written consent. Nullscape may freely assign or transfer these Terms and its rights and obligations hereunder without restriction. Subject to the foregoing, these Terms will bind and inure to the benefit of the parties, their successors, and permitted assigns."
            )

            SectionTitle("19. Contact Information")
            SectionText(
                "If you have any questions about these Terms and Conditions, please contact us:"
            )
            
            ContactInfo("Company:", "Nullscape")
            ContactInfo("Website:", "www.nullscape.in")
            ContactInfo("Email:", "we.nullscape@gmail.com")
            
            SectionText(
                "We will make reasonable efforts to respond to your inquiries in a timely manner."
            )

            SectionTitle("20. Acknowledgment")
            SectionText(
                "By using TimeSetu, you acknowledge that you have read these Terms and Conditions, understand them, and agree to be bound by them. You further acknowledge that these Terms, together with the Privacy Policy, constitute the complete and exclusive statement of the agreement between you and Nullscape regarding the Service."
            )
            SectionText(
                "If you do not agree to these Terms, you must not use TimeSetu and should uninstall the App from your device."
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
