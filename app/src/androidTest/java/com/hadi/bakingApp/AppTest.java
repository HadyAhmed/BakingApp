package com.hadi.bakingApp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.hadi.bakingApp.ui.activities.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class AppTest {
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void test() {
        Espresso.registerIdlingResources(activityTestRule.getActivity().getIdlingResource());

        onView(withId(R.id.recipes_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.list_steps)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(allOf(withId(R.id.step_description)
                , withText("Recipe Introduction")
                , isDisplayed()));

        onView(allOf(withId(R.id.next), withContentDescription("Next"), isDisplayed())).perform(click());

        onView(allOf(withId(R.id.step_description)
                , withText("1. Preheat the oven to 350\u00b0F. Butter a 9\" deep dish pie pan.")
                , isDisplayed()));

        onView(withId(R.id.fab)).perform(click());

        onView(allOf(atPosition(withId(R.id.list_ingredient), 0),
                withId(R.id.ingredient_description),
                withText("Graham Cracker crumbs"),
                isDisplayed()));

        pressBack();
        pressBack();
        pressBack();
    }

    @Test
    public void testTwoPaneMode() {
        Espresso.registerIdlingResources(activityTestRule.getActivity().getIdlingResource());

        onView(withId(R.id.recipes_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.list_steps)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(allOf(withId(R.id.step_description)
                , withText("Recipe Introduction")
                , isDisplayed()));

        onView(allOf(withId(R.id.next), withContentDescription("Next"), isDisplayed())).perform(click());

        onView(allOf(withId(R.id.step_description)
                , withText("1. Preheat the oven to 350\u00b0F. Butter a 9\" deep dish pie pan.")
                , isDisplayed()));

        onView(withId(R.id.fab)).perform(click());

        onView(allOf(atPosition(withId(R.id.list_ingredient), 0),
                withId(R.id.ingredient_description),
                withText("Graham Cracker crumbs"),
                isDisplayed()));

        pressBack();
        pressBack();
    }


    private Matcher<View> atPosition(final Matcher<View> viewMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                ViewParent viewParent = view.getParent();
                return viewParent instanceof ViewGroup
                        && viewMatcher.matches(viewParent)
                        && view.equals(((ViewGroup) viewParent).getChildAt(position));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("At position: " + position);
                viewMatcher.describeTo(description);
            }
        };
    }
}
