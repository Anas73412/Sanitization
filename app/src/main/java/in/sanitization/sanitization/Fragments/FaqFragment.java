package in.sanitization.sanitization.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.sanitization.sanitization.Adapter.FAQAdapter;
import in.sanitization.sanitization.Model.FAQModel;
import in.sanitization.sanitization.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FaqFragment extends Fragment {
    ArrayList<FAQModel>faq_list;
    FAQAdapter faqAdapter;
    RecyclerView rv_faq;

    public FaqFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_faq, container, false);
        rv_faq=view.findViewById(R.id.rv_faq);
        faq_list = new ArrayList<>();
        rv_faq.setLayoutManager(new LinearLayoutManager(getActivity()));
        faq_list.add(new FAQModel("How is it beneficial","The formula used  has proven in University and clinical studies to be one of the most effective solutions in destroying virtually any form of bacteria and virus within minutes of application. In fact, our formula is the same formula often used in wound care for both humans and pets."));
        faq_list.add(new FAQModel("Cost for Sanitization","Please call or email us to discuss your situation and we will discuss a strategy for solving you issues and how much it will cost."));
        faq_list.add(new FAQModel("Duration for the process","Normally, the pest control team would inform you about the usage of the premises again. But in general, keep the premises close for more than 10 hours for best results."));
        faq_list.add(new FAQModel("What are the precautions to be taken","Clean your hands often. Use soap and water, or an alcohol-based hand rub.\n" +
                "Maintain a safe distance from anyone who is coughing or sneezing.\n" +
                "Don’t touch your eyes, nose or mouth.\n" +
                "Cover your nose and mouth with your bent elbow or a tissue when you cough or sneeze.\n" +
                "Stay home if you feel unwell.\n" +
                "If you have a fever, cough and difficulty breathing, seek medical attention. Call in advance.\n" +
                "Follow the directions of your local health authority."));
        faq_list.add(new FAQModel("How much it is helpful for health?"," It is very much helpful for health and other persistence of human beings. Because species of pests negatively affect our lives by invading our space, damaging our property and threatening our health. Health concerns associated with structural pests include venomous stings and bites in addition to the transmission of diseases including food poisoning, allergies and Hantavirus pulmonary syndrome. Pests also can have a psychological impact on us, for example, from the unsettling feeling of knowing (or suspecting) you are living with insects, spiders, rats or mice."));
        faq_list.add(new FAQModel("When should I get it done?","We recommend calling us for 2 reasons. The first is if you have a germ outbreak like the flu, norovirus or an odor problem that you need treated. The second is to prevent outbreaks by having us come periodically to treat work or home spaces."));

                faqAdapter = new FAQAdapter(getActivity(),faq_list);
        rv_faq.setAdapter(faqAdapter);
       return view ;
    }
}
