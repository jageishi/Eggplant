module Danger
  class CheckTest < Plugin
    def check
      target_files = git.added_files + git.modified_files + git.deleted_files + git.renamed_files.map { |f| f[:after] }
      message = ""
      target_files.each do |file|
        message << "|`#{file}`|\n"
      end

      if !message.empty?
        header = "#### 追加、変更、削除されたファイル\n"
        header << "|File|\n"
        header << "| --- |\n"
        message = header + message
        markdown(message)
      end
    end
  end
end