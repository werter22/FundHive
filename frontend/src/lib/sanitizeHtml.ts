import sanitizeHtml from "sanitize-html";

export function cleanEditorHtml(html: string): string {
  return sanitizeHtml(html, {
    allowedTags: [
      "p", "span", "strong", "em", "u", "ol", "ul", "li", "br", "img", "a"
    ],
    allowedAttributes: {
      "*": ["style"],
      a: ["href", "target"],
      img: ["src", "alt", "style", "width", "height"]
    },
    allowedStyles: {
      "*": {
        "text-align": [/^left$/, /^right$/, /^center$/, /^justify$/],
        "margin": [/^auto$/],
        "display": [/^block$/],
        "width": [/^[\d.]+(px|%)$/],
        "height": [/^[\d.]+(px|%)$/],
        "font-weight": [/^bold$/],
        "font-style": [/^italic$/],
        "text-decoration": [/^underline$/]
      }
    },
    transformTags: {
      span: sanitizeHtml.simpleTransform("span", {}, (attribs) => {
        if (attribs.style) {
          attribs.style = attribs.style
            .split(";")
            .filter((s) => !s.includes("background-color") && !s.includes("color"))
            .join(";");
        }
        return attribs;
      })
    },
    disallowedTagsMode: "discard"
  });
}
