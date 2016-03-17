/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.wizardpager.wizard;

import android.content.Context;

import com.example.android.wizardpager.wizard.model.AbstractWizardModel;
import com.example.android.wizardpager.wizard.model.BranchPage;
import com.example.android.wizardpager.wizard.model.CustomerInfoPage;
import com.example.android.wizardpager.wizard.model.Page;
import com.example.android.wizardpager.wizard.model.PageList;

import java.util.ArrayList;

public class SandwichWizardModel extends AbstractWizardModel {
    public SandwichWizardModel(Context context) {
        super(context);
    }

    @Override
    protected PageList onNewRootPageList() {

        int x = 3;

        ArrayList<Page> pages= new ArrayList<Page>();

        for (int i = 1; i <= x; i++){
            JsonFormFragmentPresenter.isValid = false;
            String number ;
            if(i == 1){
                number = JsonFormConstants.FIRST_STEP_NAME;
                pages.add(new CustomerInfoPage(this,"First  " + i ,number).setRequired(true).setHasMiddleAction(true));
            } else if (i == 2){
                number = "step2";

                pages.add(new CustomerInfoPage(this,"Second  " + i ,number).setRequired(true).setHasMiddleAction(true));

            } else {
                number = "step3";
                pages.add(new CustomerInfoPage(this,"Third  " + i ,number).setRequired(true).setHasMiddleAction(true));

            }
        }



        return new PageList(

                // BranchPage shows all of the branches available: Branch One, Branch Two, Branch Three. Each of these branches
                // have their own questions and the choices of the user will be summarised in the review section at the end


                new BranchPage(this, "Select one option")
                        .addBranch("Branch One",
                              pages
                        )
//
//                                // Second branch of questions
//                        .addBranch("Branch Two",
//                                new InstructionPage(this, "Info"),
//                                new CustomerInfoPage(this, "Customer info")
//                                        .setRequired(true)
//                                        .setHasMiddleAction(true),
//                                new SingleFixedChoicePage(this, "Question One")
//                                        .setChoices("A", "B")
//                                        .setRequired(true),
//
//                                new SingleFixedChoicePage(this, "Question Two")
//                                        .setChoices("A", "B", "C",
//                                                "D", "E", "F")
//                                        .setRequired(true),
//
//                                new SingleFixedChoicePage(this, "Question Three")
//                                        .setChoices("A", "B", "C")
//                        )
//
//                                // Third branch of questions
//                        .addBranch("Branch Three",
//                                new InstructionPage(this, "Info"),
//                                new CustomerInfoPage(this, "Customer info")
//                                        .setRequired(true)
//                                        .setHasMiddleAction(true),
//                                new SingleFixedChoicePage(this, "Question One")
//                                        .setChoices("A", "B", "C")
//                                        .setRequired(true)
//                        )
        );
    }
}
