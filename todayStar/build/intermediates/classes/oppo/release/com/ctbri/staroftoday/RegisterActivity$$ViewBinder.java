// Generated code from Butter Knife. Do not modify!
package com.ctbri.staroftoday;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class RegisterActivity$$ViewBinder<T extends com.ctbri.staroftoday.RegisterActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624030, "field 'etPhone'");
    target.etPhone = finder.castView(view, 2131624030, "field 'etPhone'");
    view = finder.findRequiredView(source, 2131624019, "field 'etPassword'");
    target.etPassword = finder.castView(view, 2131624019, "field 'etPassword'");
    view = finder.findRequiredView(source, 2131624034, "field 'etBabyName'");
    target.etBabyName = finder.castView(view, 2131624034, "field 'etBabyName'");
    view = finder.findRequiredView(source, 2131624032, "field 'etValidateCode'");
    target.etValidateCode = finder.castView(view, 2131624032, "field 'etValidateCode'");
    view = finder.findRequiredView(source, 2131624033, "field 'bValidateCode'");
    target.bValidateCode = finder.castView(view, 2131624033, "field 'bValidateCode'");
    view = finder.findRequiredView(source, 2131624036, "field 'tvBabyBirthday'");
    target.tvBabyBirthday = finder.castView(view, 2131624036, "field 'tvBabyBirthday'");
    view = finder.findRequiredView(source, 2131624037, "field 'bRegister'");
    target.bRegister = finder.castView(view, 2131624037, "field 'bRegister'");
  }

  @Override public void unbind(T target) {
    target.etPhone = null;
    target.etPassword = null;
    target.etBabyName = null;
    target.etValidateCode = null;
    target.bValidateCode = null;
    target.tvBabyBirthday = null;
    target.bRegister = null;
  }
}
