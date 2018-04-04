package example.com.mydiary.view;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by saikr on 04-04-2018.
 */

public interface IAllEntryActivityCommunicator {
    void openFullEntry(@Nullable String id);

    void delete(@NotNull String id);
}
