module Danger
  class ImageResourceChecker < Plugin

    attr_accessor :target_densities, :target_extensions

    def target_densities
      return @target_densities || ["mdpi", "hdpi", "xhdpi", "xxhdpi", "xxxhdpi"]
    end

    def target_extensions
      return @target_extensions || ["png", "jpg"]
    end

    def check
      renamed_files = git.renamed_files
      target_files = git.added_files \
                        + git.modified_files \
                        + renamed_files.map { |f| f[:after] } \
                        - git.deleted_files \
                        - renamed_files.map { |f| f[:before] }

      regex = /drawable-(#{target_densities.join("|") })\/.*\.(#{target_extensions.join("|")})$/
      target_image_files = target_files.filter { |f| regex.match?(f) }

      checked_files = []
      missing_density_files = []

      target_image_files.each do |f|
        next if checked_files.include?(f)
        target_densities.each do |d|
          image_file = f.gsub(/drawable-(#{target_densities.join("|")})/, "drawable-#{d}")
          missing_density_files << image_file unless File.exists?(image_file)
          checked_files << image_file
        end
      end

      unless missing_density_files.empty?
        header = "### 画像ファイルを追加してください:pray:\n"
        header << "| ファイル |\n"
        header << "| --- |\n"
        message = missing_density_files.map { |f| "| `#{f}` |\n" }.join
        markdown(header + message)
      end
    end
  end
end