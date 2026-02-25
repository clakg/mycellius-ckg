import DOMPurify from "dompurify";
import { marked } from "marked";

export default function SafeMarkdown({ markdown }) {
  const html = DOMPurify.sanitize(marked.parse(markdown ?? ""));
  return <div dangerouslySetInnerHTML={{ __html: html }} />;
}