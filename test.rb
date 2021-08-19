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
      puts target_densities
      puts target_extensions
    end
  end
end