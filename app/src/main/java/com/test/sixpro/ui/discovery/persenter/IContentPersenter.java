package com.test.sixpro.ui.discovery.persenter;

import com.test.sixpro.bean.IndutyMustBean;
import com.test.sixpro.ui.discovery.model.IContentModel;
import com.test.sixpro.ui.discovery.model.IContentModelImpl;
import com.test.sixpro.ui.discovery.view.IContentView;

public class IContentPersenter<T extends IContentView> extends BasePersenter<T> {


    IContentModel iContentModel = new IContentModelImpl();

//    IContentView iContentView;
//    public IContentPersenter( IContentView iContentView){
//        this.iContentView = iContentView;
//    }

    public void fetch() {

        if (iContentView.get() != null && iContentModel != null) {

            iContentModel.loadContent(new IContentModel.ContentOnloadListener() {
                @Override
                public void onComplete(IndutyMustBean beans) {
                    iContentView.get().showContentView(beans);
                }

                @Override
                public void onError(Throwable e) {
                    iContentView.get().showErrorView(e);
                }
            });

        }
    }
}
