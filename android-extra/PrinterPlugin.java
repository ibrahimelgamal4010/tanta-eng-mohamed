package eg.rta.tanta;

import android.content.Context;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;

import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

/** طباعة الصفحة الحالية (تقارير / تصاريح) عن طريق نظام الطباعة في أندرويد - ومنه حفظ كـ PDF */
@CapacitorPlugin(name = "Printer")
public class PrinterPlugin extends Plugin {
    @PluginMethod
    public void print(final PluginCall call) {
        final String name = call.getString("name", "document");
        final boolean land = Boolean.TRUE.equals(call.getBoolean("landscape", false));
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    PrintManager pm = (PrintManager) getActivity().getSystemService(Context.PRINT_SERVICE);
                    PrintDocumentAdapter adapter = getBridge().getWebView().createPrintDocumentAdapter(name);
                    PrintAttributes.MediaSize size = PrintAttributes.MediaSize.ISO_A4;
                    if (land) size = size.asLandscape();
                    PrintAttributes attrs = new PrintAttributes.Builder()
                            .setMediaSize(size)
                            .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                            .build();
                    pm.print(name, adapter, attrs);
                    call.resolve();
                } catch (Exception e) {
                    call.reject(e.getMessage());
                }
            }
        });
    }
}
